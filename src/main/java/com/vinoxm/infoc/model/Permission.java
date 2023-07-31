package com.vinoxm.infoc.model;

import lombok.Getter;
import lombok.Setter;

public class Permission {

    @Setter
    @Getter
    private long id;

    @Setter
    @Getter
    private String code;

    @Setter
    @Getter
    private String desc;

    public static final long QUERY_USER_LIST = 1;
}
