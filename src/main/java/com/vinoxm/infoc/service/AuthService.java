package com.vinoxm.infoc.service;

import com.alibaba.fastjson.JSON;
import com.vinoxm.infoc.dao.AuthDao;
import com.vinoxm.infoc.model.User;
import com.vinoxm.infoc.result.BaseResult;
import com.vinoxm.infoc.result.DataResult;
import com.vinoxm.infoc.utils.TOTP;
import com.vinoxm.infoc.utils.Token;
import com.vinoxm.infoc.vo.TokenVo;
import com.vinoxm.infoc.vo.UserLoginVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
public class AuthService extends BaseService<AuthDao> {

    public BaseResult login(UserLoginVo userVo) {
        User user = baseDao.selectByUserName(userVo.getUserName());
        if (user == null) {
            return BaseResult.Failed(BaseResult.USER_NOT_EXISTS, "User Not Exists!");
        }
        if (!userVo.getPassword().equals(user.getPassword())) {
            return BaseResult.Failed(BaseResult.PASSWORD_ERROR, "Password Error!");
        }
        if (!TOTP.verifyTOTPRigidity(user.getUserName(), user.getPassword(), userVo.getKeyCode())) {
            return BaseResult.Failed(BaseResult.KEYCODE_ERROR, "KeyCode Error!");
        }
        TokenVo token = Token.generalToken(user.getUserName(), user.getPassword(), JSON.toJSONString(user));
        log.info("[Create Token] " + token);
        return DataResult.Success(token);
    }

    @Transactional
    public BaseResult addOneUser() {
        User user = new User();
        user.setUserName("zhangsan");
        user.setPassword("123456");
        baseDao.insertOneUser(user);
        return BaseResult.Success();
    }
}
