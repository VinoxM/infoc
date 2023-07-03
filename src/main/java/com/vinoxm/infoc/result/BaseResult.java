package com.vinoxm.infoc.result;

import lombok.Data;

@Data
public class BaseResult {

    private long code;
    private String message;

    public BaseResult(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResult() {
    }

    public static BaseResult Success() {
        return new BaseResult(SUCCESS, "Success!");
    }

    public static BaseResult Success(String message) {
        return new BaseResult(SUCCESS, message);
    }

    public static BaseResult Failed() {
        return new BaseResult(FAILED, "Failed!");
    }

    public static BaseResult Failed(String message) {
        return new BaseResult(FAILED, message);
    }

    public static BaseResult Failed(long code, String message) {
        return new BaseResult(code, message);
    }

    public static BaseResult ServerError() {
        return new BaseResult(SERVER_ERROR, "Server Error!");
    }

    public static BaseResult BusinessError(String message) {
        return new BaseResult(BUSINESS_ERROR, message);
    }

    public static BaseResult ValidateError(String message) {
        return new BaseResult(VALIDATE_ERROR, message);
    }

    public static BaseResult AuthError(String message) {
        return new BaseResult(AUTH_ERROR, "Auth Error: " + message);
    }

    public static final long SUCCESS = 0;
    public static final long FAILED = 500;
    public static final long SERVER_ERROR = 501;
    public static final long BUSINESS_ERROR = 502;
    public static final long VALIDATE_ERROR = 503;
    public static final long AUTH_ERROR = -1000;
    public static final long USER_NOT_EXISTS = -2001;
    public static final long PASSWORD_ERROR = -2002;
    public static final long KEYCODE_ERROR = -2003;

    @Override
    public String toString() {
        return "BaseResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
