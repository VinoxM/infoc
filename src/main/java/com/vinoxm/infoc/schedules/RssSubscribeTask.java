package com.vinoxm.infoc.schedules;

import com.vinoxm.infoc.dao.RssDao;
import com.vinoxm.infoc.model.RssResult;
import com.vinoxm.infoc.model.RssSubscribe;
import com.vinoxm.infoc.rest.RestClient;
import com.vinoxm.infoc.vo.RssVo;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

@Component
@Log4j2
public class RssSubscribeTask {

    private RssDao rssDao;

    @Autowired(required = false)
    private void setRssDao(RssDao rssDao) {
        this.rssDao = rssDao;
    }

    private List<RssSubscribe> getRssSubscribe() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int round = (Math.round((float) month / 3)) * 3 + 1;
        String season = calendar.get(Calendar.YEAR) + "-" + (round < 10 ? "0" : "") + round;
        List<RssSubscribe> list = new ArrayList<>(rssDao.selectRssSubscribeBySeason(season));
        if (month % 3 == 1) {
            if (month > 1) {
                season = calendar.get(Calendar.YEAR) + "-0" + (round - 3);
            } else {
                season = (calendar.get(Calendar.YEAR) - 1) + "-10";
            }
            list.addAll(rssDao.selectRssSubscribeBySeason(season));
        }
        return list;
    }

    private void subscribeRss(List<RssSubscribe> list) {
        for (RssSubscribe rss : list) {
            String url = rss.getUrl();
            String regex = rss.getRegex();
            try {
                String result = RestClient.getRssSubscribeByUrl(url, String.class);
                RssVo rssVo = subscribeHandler(url, result);
                if (rssVo != null) {
                    List<RssResult> results = rssVo.getRssItems(regex, rss.getId());
                    int rows = 0;
                    if (results.size() > 0) {
                        rows = rssDao.insertManyResult(results);
                    }
                    log.info(String.format("[Rss subscribe] (%s) %s -> %s new results", rss.getSeason(), rss.getName(), rows));
                }
            } catch (Exception e) {
                log.error(String.format("[Rss subscribe] (%s) %s -> \n\t%s", rss.getSeason(), rss.getName(), e.getMessage()));
            }
        }
    }

    private RssVo subscribeHandler(String url, String result) {
        try {
            String host = new URL(url).getHost();
            if (Pattern.matches(".*dmhy.*", host)) {
                JSONObject json = XML.toJSONObject(result);
                return com.alibaba.fastjson.JSONObject.parseObject(json.toString()).toJavaObject(RssVo.class);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Scheduled(cron = "${schedule.corn}")
//    @Scheduled(fixedDelay = 20000)
    public void updateRssSubscribe() {
        log.info("[Schedule start] Update Rss Subscribe");
        List<RssSubscribe> list = getRssSubscribe();
        subscribeRss(list);
    }
}
