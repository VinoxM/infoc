package com.vinoxm.infoc.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
@ConditionalOnClass(RestProxyConfig.class)
public class RestConfiguration {
    private RestProxyConfig proxyConfig;
    @Autowired
    private void setProxyConfig(RestProxyConfig proxyConfig){
        this.proxyConfig = proxyConfig;
    }

    @Value("${rest.readTimeout}")
    private int readTimeout;

    @Value("${rest.connectTimeout}")
    private int connectTimeout;

    @Bean
    public SimpleClientHttpRequestFactory httpClientFactory() {
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setReadTimeout(readTimeout);
        httpRequestFactory.setConnectTimeout(connectTimeout);
        if (proxyConfig.isEnable()) {
            InetSocketAddress socketAddress = new InetSocketAddress(proxyConfig.getHost(), proxyConfig.getPort());
            Proxy proxy = new Proxy(Proxy.Type.HTTP, socketAddress);
            httpRequestFactory.setProxy(proxy);
        }
        return httpRequestFactory;
    }

    @Bean
    public RestTemplate restTemplate(SimpleClientHttpRequestFactory httpRequestFactory) {
        return new RestTemplate(httpRequestFactory);
    }
}
