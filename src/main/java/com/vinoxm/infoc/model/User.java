package com.vinoxm.infoc.model;

import lombok.Data;

import javax.persistence.*;

@Table(name = "admin_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "user_name")
    private String userName;
    private String password;
}
