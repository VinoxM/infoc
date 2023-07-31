package com.vinoxm.infoc.redis;

import com.vinoxm.infoc.utils.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class RedisUtils {

    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

    public static RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * key是否存在
     * @param key 键名
     * @return true|false
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 为key设置过期时间
     * @param key 键名
     * @param time 过期时间,单位s
     * @return ture|false
     */
    public Boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 获取key的过期时间
     * @param key 键名
     * @return 过期时间,单位s
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 移除kye的过期时间
     * @param key 键名
     * @return true|false
     */
    public Boolean persist(String key) {
        return redisTemplate.boundValueOps(key).persist();
    }

    public Boolean delKey(String key) {
        return redisTemplate.delete(key);
    }

    // -------- String --------

    public Object get(String key) {
        return StringUtils.isEmpty(key) ? null : redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, long expire) {
        if (expire > 0 ) {
            redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
        } else {
            set(key, value);
        }
    }

    public void batchSet(Map<String, Object> keyAndValue) {
        redisTemplate.opsForValue().multiSet(keyAndValue);
    }

    public void batchSetIfAbsent(Map<String, Object> keyAndValue) {
        redisTemplate.opsForValue().multiSetIfAbsent(keyAndValue);
    }

    public Long increment(String key, long number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    public Double increment(String key, double number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    // -------- Set --------

    public void sSet(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public void sSet(String key, Object... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    public void sRemove(String key, Object value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public Long size(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public Boolean isMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    // -------- Hash --------

    public void add(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public Object getHashValue(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public Map<Object, Object> getHashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public Set<Object> getHashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    public Long hashSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    public Boolean containsHashKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    // -------- List --------

    public void push(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public void pushIfAbsent(String key, Object value) {
        redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    public void pushAll(String key, List<Object> list) {
        redisTemplate.opsForList().rightPushAll(key, list);
    }

    public void pushAllIfAbsent(String key, List<Object> value) {
        redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    public Object getIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    public List<Object> getList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }
}
