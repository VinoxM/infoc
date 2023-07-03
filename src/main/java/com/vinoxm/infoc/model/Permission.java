package com.vinoxm.infoc.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "admin_permission")
public class Permission {

    @Id
    @Setter
    @Getter
    private long id;

    @Setter
    @Getter
    private String code;

    @Column(name = "desc_")
    @Setter
    @Getter
    private String desc;

    public static final long QUERY_USER_LIST = 1;
}
