package com.vinoxm.infoc.controller;

import com.vinoxm.infoc.result.BaseResult;
import com.vinoxm.infoc.result.DataResult;
import com.vinoxm.infoc.service.AuthService;
import com.vinoxm.infoc.vo.UserLoginVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController extends BaseController <AuthService>{

    @GetMapping("login")
    public BaseResult login(UserLoginVo loginVo) {
        return baseService.login(loginVo);
    }

    @GetMapping("test")
    public BaseResult test() {
        return DataResult.Success(null);
    }

    @GetMapping("test1")
    public BaseResult test1() {
        return baseService.addOneUser();
    }

}
