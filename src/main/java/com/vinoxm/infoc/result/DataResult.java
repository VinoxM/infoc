package com.vinoxm.infoc.result;

import lombok.Data;

@Data
public class DataResult<T> extends BaseResult {
    private long code;
    private String message;
    private T data;

    public DataResult() {
    }

    public DataResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static BaseResult Success(Object data) {
        return new DataResult<>(BaseResult.SUCCESS, "Success!", data);
    }

    public static BaseResult Success(Object data, String message) {
        return new DataResult<>(BaseResult.SUCCESS, message, data);
    }

    @Override
    public String toString() {
        return "DataResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
