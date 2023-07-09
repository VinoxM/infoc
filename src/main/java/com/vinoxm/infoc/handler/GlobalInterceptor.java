package com.vinoxm.infoc.handler;

import com.alibaba.fastjson.JSON;
import com.vinoxm.infoc.annotions.NeedAuth;
import com.vinoxm.infoc.annotions.NeedSecret;
import com.vinoxm.infoc.exceptions.AuthException;
import com.vinoxm.infoc.model.User;
import com.vinoxm.infoc.utils.BaseContextHolder;
import com.vinoxm.infoc.utils.StringUtils;
import com.vinoxm.infoc.utils.Token;
import com.vinoxm.infoc.vo.TokenVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Log4j2
public class GlobalInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod mHandler = (HandlerMethod) handler;

            // Author validate
            NeedAuth needAuth = mHandler.getMethodAnnotation(NeedAuth.class);
            if (needAuth != null) {
                String token = request.getHeader("token");
                String salt = request.getHeader("salt");
                if (StringUtils.isEmpty(token) || StringUtils.isEmpty(salt)) {
                    throw new AuthException("[" + request.getMethod() + "] " + request.getRequestURI(), "Token or Salt is empty ");
                }
                try {
                    log.info("[Verify token] " + token);
                    Token.verifyToken(new TokenVo(token, salt));
                } catch (Exception e) {
                    throw new AuthException("[" + request.getMethod() + "] " + request.getRequestURI(), e.getMessage());
                }
                String json = Token.getClaimJson(token);
                User user = JSON.parseObject(json,User.class);
                BaseContextHolder.setUserInfo(user);
                BaseContextHolder.setToken(token);
                log.info("[Verify token] Success");
            }

            // Secret validate
            NeedSecret needSecret = mHandler.getMethodAnnotation(NeedSecret.class);
            if (needSecret != null) {
                String secret = request.getHeader("secret");
                if (StringUtils.isEmpty(secret)) {
                    throw new AuthException("[" + request.getMethod() + "] " + request.getRequestURI(), "Secret is empty ");
                }
                log.info("[Verify Secret] " + secret);
                if (!secret.equals(needSecret.value())) {
                    throw new AuthException("[" + request.getMethod() + "] " + request.getRequestURI(), "Secret error ");
                }
                log.info("[Verify Secret] Success");
            }
        }
        log.info("Access request: [" + request.getMethod() + "] " + request.getRequestURI());
        return true;
    }
}
