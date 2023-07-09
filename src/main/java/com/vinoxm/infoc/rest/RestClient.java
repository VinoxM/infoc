package com.vinoxm.infoc.rest;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;

@Component
@Log4j2
public class RestClient {

    private static RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        RestClient.restTemplate = restTemplate;
    }

    public static <T> T get(String url, Class<T> clazz) {
        return restTemplate.getForObject(url, clazz);
    }

    public static <T> T getWithParams(String url, Class<T> clazz, HashMap<String, Object> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
        headers.set(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, clazz, params).getBody();
    }

    public static <T> T getRssSubscribeByUrl(String url, Class<T> clazz) throws Exception {
        try {
            URL u = new URL(url);
            String query = u.getQuery();
            StringBuilder uri = new StringBuilder(String.format("%s://%s%s", u.getProtocol(), u.getAuthority(), u.getPath()));
            HashMap<String, Object> params = new HashMap<>();
            if (query != null) {
                uri.append("?");
                for (String q : query.split("&")) {
                    String[] split = q.split("=");
                    params.put(split[0], URLDecoder.decode(split[1], "utf-8"));
                    uri.append(String.format("%s={%s}&", split[0], split[0]));
                }
            }
            log.info(String.format("[RestClient request] %s", URLDecoder.decode(url, "utf-8")));
            return restTemplate.getForObject(uri.toString(), clazz, params);
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
