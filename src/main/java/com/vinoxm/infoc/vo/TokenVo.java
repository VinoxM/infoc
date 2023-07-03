package com.vinoxm.infoc.vo;

import lombok.Data;

@Data
public class TokenVo {
    private String token;
    private String salt;

    public TokenVo() {
    }

    public TokenVo(String token, String salt) {
        this.token = token;
        this.salt = salt;
    }
}
