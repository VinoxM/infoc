package com.vinoxm.infoc.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.vinoxm.infoc.exceptions.AuthException;
import com.vinoxm.infoc.vo.TokenVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Log4j2
@Component
public class Token {

    private static long expire;

    @Value("${jwt.token.expireTime}")
    public void setExpire(String expireTime) {
        String[] split = expireTime.split("\\*");
        long sum = 1;
        for (String s : split) {
            int num = Integer.parseInt(s);
            sum *= num;
        }
        expire = sum;
    }

    public static TokenVo generalToken(String uName, String pass, String json) {
        String code = uName + '-' + pass;
        String salt = UUID.randomUUID().toString().substring(7);
        long now = new Date().getTime();
        Date expireDate = new Date(now + expire);
        String token = JWT.create().withAudience(code).withClaim("json", json).withExpiresAt(expireDate).sign(Algorithm.HMAC256(salt));
        return new TokenVo(token, salt);
    }

    public static void verifyToken(TokenVo tokenVo) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(tokenVo.getSalt())).build();
        try {
            verifier.verify(tokenVo.getToken());
        } catch (JWTVerificationException e) {
            throw new AuthException(null, e.getMessage());
        }
    }

    public static String getClaimJson(String token){
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("json").asString();
    }
}
