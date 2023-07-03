package com.vinoxm.infoc.handler;

import com.vinoxm.infoc.result.BaseResult;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.net.URL;

@ControllerAdvice
@Log4j2
public class ResponseBodyAnalysis implements ResponseBodyAdvice<BaseResult> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return  BaseResult.class.isAssignableFrom(returnType.getParameterType());
    }

    @SneakyThrows
    @Override
    public BaseResult beforeBodyWrite(BaseResult baseResult, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        log.info("Request return: [" + serverHttpRequest.getMethod() + "] " + serverHttpRequest.getURI().getPath() +" -> "+ (baseResult == null ? "Nothing" : baseResult.toString()));
        return baseResult;
    }
}
