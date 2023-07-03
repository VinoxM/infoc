package com.vinoxm.infoc.handler;

import com.vinoxm.infoc.exceptions.AuthException;
import com.vinoxm.infoc.exceptions.BusinessException;
import com.vinoxm.infoc.exceptions.ValidationException;
import com.vinoxm.infoc.result.BaseResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    BaseResult handleException(Exception e) {
        log.error(e);
        return BaseResult.ServerError();
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    BaseResult handleBusinessException(BusinessException e) {
        log.error("[Business error] " + e.getMessage());
        return BaseResult.BusinessError(e.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    @ResponseBody
    BaseResult handleAuthException(AuthException e) {
        log.error("[Auth error] " + e.getUrl() + " : " + e.getMessage());
        return BaseResult.AuthError(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    BaseResult handleValidationException(ValidationException e) {
        log.error("[Validate error] " + e.getMessage());
        return BaseResult.ValidateError(e.getMessage());
    }
}
