package com.vinoxm.infoc.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vinoxm.infoc.dao.RssDao;
import com.vinoxm.infoc.model.RssResult;
import com.vinoxm.infoc.model.RssSubscribe;
import com.vinoxm.infoc.rest.RestClient;
import com.vinoxm.infoc.result.BaseResult;
import com.vinoxm.infoc.result.DataResult;
import com.vinoxm.infoc.vo.PagerVo;
import com.vinoxm.infoc.vo.RssVo;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;


@Service
@Log4j2
public class RssService extends BaseService<RssDao> {

    public BaseResult addOneRssSubs(RssSubscribe rssSubscribe) {
        baseDao.insertOne(rssSubscribe);
        return BaseResult.Success();
    }

    public BaseResult addManyRssSubs(RssSubscribe[] rssArray) {
        List<RssSubscribe> list = Arrays.asList(rssArray);
        baseDao.insertMany(list);
        return BaseResult.Success();
    }

    public BaseResult delOneRssSubs(RssSubscribe rssSubscribe) {
        baseDao.deleteOne(rssSubscribe);
        return BaseResult.Success();
    }

    public BaseResult delOneRssSubsResultsById(RssSubscribe rssSubscribe) {
        baseDao.deleteManyRssResultByPid(rssSubscribe.getId());
        return BaseResult.Success();
    }

    public BaseResult delManyRssSubs(RssSubscribe[] rssArray) {
        List<RssSubscribe> list = Arrays.asList(rssArray);
        baseDao.deleteMany(list);
        return BaseResult.Success();
    }

    public BaseResult selOneRssSubs(long id) {
        return DataResult.Success(baseDao.selectOneById(id));
    }

    public BaseResult selSearchRssSubsWithPage(PagerVo<RssSubscribe> pager, HashMap<String, Object> params) {
        Page<Object> page = PageHelper.startPage(pager.getPageNum(), pager.getPageSize());
        List<RssSubscribe> list = baseDao.selectAll(params);
        pager.setData(list);
        pager.setTotal(page.getTotal());
        return DataResult.Success(pager);
    }

    public BaseResult selSearchRssSubs(HashMap<String, Object> params) {
        return DataResult.Success(baseDao.selectAll(params));
    }

    public BaseResult editOneRssSubsById(RssSubscribe rssSubscribe) {
        RssSubscribe old = baseDao.selectOneById(rssSubscribe.getId());
        if (!Objects.equals(old.getUrl(), rssSubscribe.getUrl()) || !Objects.equals(old.getRegex(), rssSubscribe.getRegex())) {
            baseDao.deleteManyRssResultByPid(old.getId());
        }
        baseDao.updateOneById(rssSubscribe);
        return BaseResult.Success();
    }

    public BaseResult selSeason() {
        return DataResult.Success(baseDao.selRssSubscribeSeason());
    }

    public List<RssSubscribe> getRssSubscribe() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int round = (Math.round((float) month / 3)) * 3 + 1;
        String season = calendar.get(Calendar.YEAR) + "-" + (round < 10 ? "0" : "") + round;
        List<RssSubscribe> list = new ArrayList<>(baseDao.selectRssSubscribeBySeason(season));
        if (month % 3 == 1) {
            if (month > 1) {
                season = calendar.get(Calendar.YEAR) + "-0" + (round - 3);
            } else {
                season = (calendar.get(Calendar.YEAR) - 1) + "-10";
            }
            list.addAll(baseDao.selectRssSubscribeBySeason(season));
        }
        return list;
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

    public void subscribeRss(List<RssSubscribe> list) {
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
                        rows = baseDao.insertManyResult(results);
                    }
                    log.info(String.format("[Rss subscribe] (%s) %s -> %s new results", rss.getSeason(), rss.getName(), rows));
                }
            } catch (Exception e) {
                log.error(String.format("[Rss subscribe] (%s) %s -> \n\t%s", rss.getSeason(), rss.getName(), e.getMessage()));
            }
        }
    }

    public List<RssResult> subscribeRssTest(RssSubscribe rss) {
        List<RssResult> results = new ArrayList<>();
        String url = rss.getUrl();
        String regex = rss.getRegex();
        try {
            String result = RestClient.getRssSubscribeByUrl(url, String.class);
            RssVo rssVo = subscribeHandler(url, result);
            if (rssVo != null) {
                results = rssVo.getRssItems(regex, rss.getId());
            }
        } catch (Exception e) {
            log.error(String.format("[Rss Subscribe Test Error] (%s) %s -> \n\t%s", rss.getSeason(), rss.getName(), e.getMessage()));
        }
        return results;
    }
}
