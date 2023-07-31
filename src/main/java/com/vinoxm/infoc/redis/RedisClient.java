package com.vinoxm.infoc.redis;

import com.alibaba.fastjson.JSON;
import com.vinoxm.infoc.model.RssSubscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RedisClient {

    private static long expire;

    @Value("${redis.rss.expireTime}")
    public void setExpire(String expireTime) {
        String[] split = expireTime.split("\\*");
        long sum = 1;
        for (String s : split) {
            int num = Integer.parseInt(s);
            sum *= num;
        }
        expire = sum;
    }

    private final static String RSS_UPDATING = "rss-updating";
    private final static String RSS_FLUSH_SEASON = "rss-flush-season";
    private final static String RSS_SEASON_HASH = "rss-season-hash";
    private final static String RSS_SEASON_OPTIONS = "rss-season-options";

    private RedisUtils redisUtils;

    @Autowired
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    public void flushRssSubsBySeason(List<RssSubscribe> rssArray, String season) {
        Map<String, Object> map = new HashMap<>();
        map.put(season, JSON.toJSONString(rssArray));
        redisUtils.add(RSS_SEASON_HASH, map);
        redisUtils.expire(RSS_SEASON_HASH, expire);
    }

    public Boolean hasRssSubsSeason(String season) {
        return redisUtils.containsHashKey(RSS_SEASON_HASH, season);
    }

    public List<RssSubscribe> getRssSubsBySeason(String season) {
        String json = redisUtils.getHashValue(RSS_SEASON_HASH, season).toString();
        return JSON.parseArray(json, RssSubscribe.class);
    }

    public void setFlushSeasons(Collection<String> season) {
        redisUtils.sSet(RSS_FLUSH_SEASON, season.toArray());
    }

    public void setFlushSeason(String season) {
        redisUtils.sSet(RSS_FLUSH_SEASON, season);
    }

    public Set<Object> getFlushSeason() {
        return redisUtils.members(RSS_FLUSH_SEASON);
    }

    public Boolean isFlushSeason(String season) {
        return redisUtils.isMember(RSS_FLUSH_SEASON, season);
    }

    public void removeFlushSeason(String season) {
        redisUtils.sRemove(RSS_FLUSH_SEASON, season);
    }

    public boolean isRssUpdating() {
        return redisUtils.hasKey(RSS_UPDATING);
    }

    public void setRssUpdating(boolean flag) {
        if (flag) {
            redisUtils.set(RSS_UPDATING, 1, expire);
        } else {
            redisUtils.delKey(RSS_UPDATING);
        }
    }

    public void setSeasonOptions(List<String> seasonOps) {
        redisUtils.sSet(RSS_SEASON_OPTIONS, seasonOps.toArray());
    }

    public Boolean hasSeasonOptions() {
        return redisUtils.hasKey(RSS_SEASON_OPTIONS);
    }

    public Set<Object> getSeasonOptions() {
        return redisUtils.members(RSS_SEASON_OPTIONS);
    }

    public Boolean isSeasonOptionsMember(String season) {
        return redisUtils.isMember(RSS_SEASON_OPTIONS, season);
    }
}
