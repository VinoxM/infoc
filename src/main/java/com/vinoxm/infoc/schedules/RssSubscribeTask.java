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
        String season = "2023-04";//new SimpleDateFormat("yyyy-MM").format(new Date());
        List<RssSubscribe> list = rssDao.getRssSubscribeBySeason(season);
//        log.info(String.format("getRssSubscribe -> %s", list.toString()));
        return list;
    }

    private void subscribeRss(List<RssSubscribe> list) {
        for (RssSubscribe rss : list) {
            String url = rss.getUrl();
            String regex = rss.getRegex();
            String result = RestClient.getRssSubscribeByUrl(url, String.class);
            RssVo rssVo = subscribeHandler(url, result);
            if (rssVo != null) {
                List<RssResult> results = rssVo.getRssItems(regex, rss.getId());
                rssDao.insertManyResult(results);
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

    @Scheduled(fixedDelay = 100000)
    public void test() {
        List<RssSubscribe> list = getRssSubscribe();
        subscribeRss(list);
    }
}
