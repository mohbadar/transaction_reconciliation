package com.tutuka.lib.audit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuditableAspect {


    @Before("@annotation(com.tutuka.lib.audit.Auditable)")
    public void annotatedBeforeLoggingAdvice(JoinPoint joinPoint) throws Throwable {
        String desc = "[" + joinPoint.getSignature().getDeclaringTypeName() + "]" +
                "[" + ((MethodSignature) joinPoint.getSignature()).getMethod().getName() + "] ";
        log.info(desc);

        //Store object to database

    }

}