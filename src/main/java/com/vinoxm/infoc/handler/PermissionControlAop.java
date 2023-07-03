package com.vinoxm.infoc.handler;

import com.vinoxm.infoc.annotions.NeedPermission;
import com.vinoxm.infoc.model.User;
import com.vinoxm.infoc.utils.BaseContextHolder;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@Log4j2
public class PermissionControlAop {

    @Pointcut("@annotation(com.vinoxm.infoc.annotions.NeedPermission)")
    public void handlePermission() {
    }

    @Before(value = "handlePermission()")
    public void permissionInterceptor(JoinPoint joinPoint) {
        Class<?> aClass = joinPoint.getTarget().getClass();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        Class<?>[] argsTypes = signature.getParameterTypes();
        try {
            Method method = aClass.getMethod(methodName, argsTypes);
            if (method.isAnnotationPresent(NeedPermission.class)) {
                NeedPermission annotation = method.getAnnotation(NeedPermission.class);
                long[] perms = annotation.value();
                if (perms.length > 0) {
                    User user = BaseContextHolder.getUserInfo();
                    log.info(user);
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }
}
