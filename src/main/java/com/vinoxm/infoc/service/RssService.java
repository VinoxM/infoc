package com.vinoxm.infoc.service;

import com.alibaba.fastjson.JSONException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vinoxm.infoc.dao.RssDao;
import com.vinoxm.infoc.model.RssResult;
import com.vinoxm.infoc.model.RssSubscribe;
import com.vinoxm.infoc.redis.RedisClient;
import com.vinoxm.infoc.rest.RestClient;
import com.vinoxm.infoc.result.BaseResult;
import com.vinoxm.infoc.result.DataResult;
import com.vinoxm.infoc.vo.PagerVo;
import com.vinoxm.infoc.vo.RssBase;
import com.vinoxm.infoc.vo.RssSingleVo;
import com.vinoxm.infoc.vo.RssVo;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;


@Service
@Log4j2
public class RssService extends BaseService<RssDao> {

    RedisClient redisClient;

    @Autowired
    public void setRedisClient(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    public BaseResult addOneRssSubs(RssSubscribe rssSubscribe) {
        baseDao.insertOne(rssSubscribe);
        Set<String> seasons = getNeedUpdateSeason();
        String season = rssSubscribe.getSeason();
        if (seasons.contains(season)) {
            redisClient.setFlushSeason(season);
        } else {
            updateSubscribe(rssSubscribe);
        }
        if (!redisClient.isSeasonOptionsMember(season)) {
            redisClient.setSeasonOptions(baseDao.selectRssSubscribeSeason());
        }
        return BaseResult.Success();
    }

    public BaseResult addManyRssSubs(RssSubscribe[] rssArray) {
        List<RssSubscribe> list = Arrays.asList(rssArray);
        baseDao.insertMany(list);
        Set<String> s = new HashSet<>();
        for (RssSubscribe r : list) {
            s.add(r.getSeason());
        }
        redisClient.setFlushSeasons(s);
        redisClient.setSeasonOptions(baseDao.selectRssSubscribeSeason());
        return BaseResult.Success();
    }

    public BaseResult delOneRssSubs(RssSubscribe rssSubscribe) {
        RssSubscribe subs = baseDao.selectOneById(rssSubscribe.getId());
        if (subs != null) {
            baseDao.deleteOne(rssSubscribe);
            String season = subs.getSeason();
            redisClient.setFlushSeason(season);
            redisClient.setSeasonOptions(baseDao.selectRssSubscribeSeason());
        }
        return BaseResult.Success();
    }

    public BaseResult delOneRssSubsResultsById(RssSubscribe rssSubscribe) {
        RssSubscribe subs = baseDao.selectOneById(rssSubscribe.getId());
        if (subs != null) {
            baseDao.deleteManyRssResultByPid(rssSubscribe.getId());
            redisClient.setFlushSeason(subs.getSeason());
        }
        return BaseResult.Success();
    }

    public BaseResult delManyRssSubs(RssSubscribe[] rssArray) {
        List<RssSubscribe> list = Arrays.asList(rssArray);
        baseDao.deleteMany(list);
        List<String> seasons = baseDao.selectRssSubscribeSeason();
        redisClient.setFlushSeasons(seasons);
        redisClient.setSeasonOptions(baseDao.selectRssSubscribeSeason());
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

    /**
     * 按订阅名搜索直接从数据库查询
     * 仅季度查询时,则从缓存查询
     * 缓存查询前,先刷新缓存中需要刷新的季度结果缓存
     * 获取所有季度时,需先刷新所有季度的缓存结果
     *
     * @param params {
     *               name: 按RSS订阅名模糊搜索 (nullable),
     *               season: RSS订阅季度 (nullable)
     *               }
     * @return 返回符合的结果集
     */
    public BaseResult selSearchRssSubs(HashMap<String, Object> params) {
        List<RssSubscribe> list = new ArrayList<>();
        // 是否是搜索结果
        if (!params.containsKey("name")) {
            // 是否查询特定季度
            if (params.containsKey("season")) {
                String season = params.get("season").toString();
                // 是否需要刷新该季度RSS
                if (redisClient.isFlushSeason(season)) {
                    // 刷新该季度RSS订阅
                    list = baseDao.selectRssSubscribeBySeason(season);
                    redisClient.flushRssSubsBySeason(list, season);
                    redisClient.removeFlushSeason(season);
                } else {
                    // 缓存中是否存在该季度RSS
                    if (!redisClient.hasRssSubsSeason(season)) {
                        list = baseDao.selectRssSubscribeBySeason(season);
                        redisClient.flushRssSubsBySeason(list, season);
                    } else {
                        list = redisClient.getRssSubsBySeason(season);
                    }
                }
            } else {
                // 先检查需要刷新的季度缓存
                Set<Object> seasons = redisClient.getFlushSeason();
                if (seasons != null) {
                    // 刷新季度缓存
                    for (Object s : seasons) {
                        String season = s.toString();
                        List<RssSubscribe> l = baseDao.selectRssSubscribeBySeason(season);
                        redisClient.flushRssSubsBySeason(l, season);
                        redisClient.removeFlushSeason(season);
                    }
                }
                // 获取所有季度RSS订阅
                List<String> strings = baseDao.selectRssSubscribeSeason();
                if (strings != null) {
                    for (String s : strings) {
                        // 检查该季度是否有缓存
                        if (!redisClient.hasRssSubsSeason(s)) {
                            // 刷新缓存
                            List<RssSubscribe> l = baseDao.selectRssSubscribeBySeason(s);
                            redisClient.flushRssSubsBySeason(l, s);
                            list.addAll(l);
                        } else {
                            list.addAll(redisClient.getRssSubsBySeason(s));
                        }
                    }
                }
            }
        } else {
            // 直接查询搜索结果
            list = baseDao.selectAll(params);
        }
        return DataResult.Success(list);
    }

    public BaseResult editOneRssSubsById(RssSubscribe rssSubscribe) {
        RssSubscribe old = baseDao.selectOneById(rssSubscribe.getId());
        if (!Objects.equals(old.getUrl(), rssSubscribe.getUrl()) || !Objects.equals(old.getRegex(), rssSubscribe.getRegex())) {
            baseDao.deleteManyRssResultByPid(old.getId());
        }
        baseDao.updateOneById(rssSubscribe);
        String season = rssSubscribe.getSeason();
        Set<String> seasons = getNeedUpdateSeason();
        if (seasons.contains(season)) {
            redisClient.setFlushSeason(rssSubscribe.getSeason());
        } else {
            updateSubscribe(rssSubscribe);
        }
        if (!redisClient.isSeasonOptionsMember(season)) {
            redisClient.setSeasonOptions(baseDao.selectRssSubscribeSeason());
        }
        return BaseResult.Success();
    }

    public BaseResult selSeason() {
        if (redisClient.hasSeasonOptions()) {
            return DataResult.Success(redisClient.getSeasonOptions());
        } else {
            List<String> list = baseDao.selectRssSubscribeSeason();
            redisClient.setSeasonOptions(list);
            return DataResult.Success(list);
        }
    }

    public List<RssSubscribe> getRssSubscribe() {
        Set<String> seasons = getNeedUpdateSeason();
        List<RssSubscribe> list = new ArrayList<>();
        for (String season : seasons) {
            list.addAll(baseDao.selectRssSubscribeBySeason(season));
        }
        return list;
    }

    private Set<String> getNeedUpdateSeason() {
        Set<String> set = new HashSet<>();
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int round = (Math.round((float) month / 3)) * 3 + 1;
        String season = calendar.get(Calendar.YEAR) + "-" + (round < 10 ? "0" : "") + round;
        set.add(season);
        if (month % 3 == 1) {
            if (month > 1) {
                season = calendar.get(Calendar.YEAR) + "-0" + (round - 3);
            } else {
                season = (calendar.get(Calendar.YEAR) - 1) + "-10";
            }
            set.add(season);
        }
        return set;
    }

    private RssBase subscribeHandler(String url, String result) {
        RssBase rssVo = null;
        try {
            String host = new URL(url).getHost();
            if (Pattern.matches(".*dmhy.*", host)) {
                JSONObject json = XML.toJSONObject(result);
                try {
                    rssVo = com.alibaba.fastjson.JSONObject.parseObject(json.toString()).toJavaObject(RssVo.class);
                } catch (JSONException e) {
                    rssVo = com.alibaba.fastjson.JSONObject.parseObject(json.toString()).toJavaObject(RssSingleVo.class);
                }
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return rssVo;
    }

    public void subscribeRss(List<RssSubscribe> list) {
        redisClient.setRssUpdating(true);
        for (RssSubscribe rss : list) {
            String url = rss.getUrl();
            String regex = rss.getRegex();
            try {
                String result = RestClient.getRssSubscribeByUrl(url, String.class);
                RssBase rssVo = subscribeHandler(url, result);
                if (rssVo != null) {
                    List<RssResult> results = rssVo.getRssItems(regex, rss.getId());
                    int rows = 0;
                    if (results.size() > 0) {
                        rows = baseDao.insertManyResult(results);
                    }
                    if (rows > 0) {
                        redisClient.setFlushSeason(rss.getSeason());
                    }
                    log.info(String.format("[Rss subscribe] (%s) %s -> %s new results", rss.getSeason(), rss.getName(), rows));
                }
            } catch (Exception e) {
                log.error(String.format("[Rss subscribe] (%s) %s -> \n\t%s", rss.getSeason(), rss.getName(), e.getMessage()));
                redisClient.setRssUpdating(false);
            }
        }
        redisClient.setRssUpdating(false);
    }

    public BaseResult updateSubscribe(RssSubscribe r) {
        RssSubscribe rss = baseDao.selectOneById(r.getId());
        ArrayList<RssSubscribe> list = new ArrayList<>();
        list.add(rss);
        subscribeRss(list);
        return BaseResult.Success();
    }

    public List<RssResult> subscribeRssTest(RssSubscribe rss) {
        List<RssResult> results = new ArrayList<>();
        String url = rss.getUrl();
        String regex = rss.getRegex();
        try {
            String result = RestClient.getRssSubscribeByUrl(url, String.class);
            RssBase rssVo = subscribeHandler(url, result);
            if (rssVo != null) {
                results = rssVo.getRssItems(regex, rss.getId());
            }
        } catch (Exception e) {
            log.error(String.format("[Rss Subscribe Test Error] (%s) %s -> \n\t%s", rss.getSeason(), rss.getName(), e.getMessage()));
            throw new RuntimeException(e);
        }
        return results;
    }
}
