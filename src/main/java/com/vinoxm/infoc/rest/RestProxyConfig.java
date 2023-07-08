package com.vinoxm.infoc.rest;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class RestProxyConfig {
    @Value("${rest.proxy.enable}")
    private boolean enable;
    @Value("${rest.proxy.host}")
    private String host;
    @Value("${rest.proxy.port}")
    private Integer port;
}
