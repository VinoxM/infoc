package com.vinoxm.infoc.validator;

import com.vinoxm.infoc.exceptions.ValidationException;
import com.vinoxm.infoc.utils.StringUtils;
import com.vinoxm.infoc.vo.UserLoginVo;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserLoginValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserLoginVo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserLoginVo user = (UserLoginVo) target;
        if (StringUtils.isEmpty(user.getUserName())) {
            throw new ValidationException("userName is empty");
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            throw new ValidationException("password is empty");
        }
        if (StringUtils.isEmpty(user.getKeyCode())) {
            throw new ValidationException("keyCode is empty");
        }
    }
}
