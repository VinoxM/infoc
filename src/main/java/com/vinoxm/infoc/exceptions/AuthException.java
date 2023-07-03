package com.vinoxm.infoc.exceptions;

import lombok.Getter;
import lombok.Setter;

public class AuthException extends RuntimeException {
    @Getter@Setter
    private String url;

    public AuthException(String requestUrl, String reason) {
        super(reason);
        this.url = requestUrl;
    }
}
