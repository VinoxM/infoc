package com.vinoxm.infoc.utils;

import com.vinoxm.infoc.model.User;

import java.util.HashMap;
import java.util.Map;

public class BaseContextHolder {
    static final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    public BaseContextHolder() {
    }

    public static Object get(String key) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<>();
            threadLocal.set(map);
        }
        return map.get(key);
    }

    public static void set(String key, Object obj) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<>();
            threadLocal.set(map);
        }
        map.put(key, obj);
    }

    public static User getUserInfo() {
        return (User) threadLocal.get().get("user");
    }

    public static String getToken() {
        return (String) threadLocal.get().get("token");
    }

    public static void setUserInfo(User user) {
        set("user", user);
    }

    public static void setToken(String token) {
        set("token", token);
    }

    public static void remove() {
        threadLocal.remove();
    }
}
