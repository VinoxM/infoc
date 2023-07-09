package com.vinoxm.infoc.vo;

import lombok.Data;

import java.util.List;

@Data
public class PagerVo<T> {

    private int pageNum;

    private int pageSize;

    private long total;

    private List<T> data;

    @Override
    public String toString() {
        return "PagerVo{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", total=" + total +
                ", data=" + data +
                '}';
    }
}
