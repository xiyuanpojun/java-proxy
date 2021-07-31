package com.liangqiang.manager.aspect;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.liangqiang.manager.anno.ResubmitCheck;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Order(1)
@Aspect
@Component
public class ResubmitAspect {

    @Pointcut("@annotation(com.liangqiang.manager.anno.ResubmitCheck)")
    public void pointCutMethod() {
    }

    @Before(value = "pointCutMethod()")
    public void doBefore(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        String className = method.getDeclaringClass().getName();
        String name = method.getName();
        ResubmitCheck resubmitCheck = method.getDeclaredAnnotation(ResubmitCheck.class);
        if (Objects.isNull(resubmitCheck)) {
            return;
        }
        int expire = resubmitCheck.expire();
        Object[] args = point.getArgs();
        if (args == null || args.length == 0) return;

        log.info("ResubmitCheck");
    }
}
