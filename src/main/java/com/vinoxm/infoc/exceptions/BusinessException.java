package com.vinoxm.infoc.exceptions;

public class BusinessException extends RuntimeException {
    public BusinessException(String reason) {
        super(reason);
    }
}
