package com.tutuka.lib.logger.annotation;

import com.tutuka.lib.json.JsonUtility;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LoggingAspect {

    Logger log = LoggerFactory.getLogger(LoggingAspect.class.getName());

    @Before("@annotation(com.tutuka.lib.logger.annotation.Loggable)")
    public void annotatedBeforeLoggingAdvice(JoinPoint joinPoint) throws Throwable{
        log.info("[" + joinPoint.getSignature().getDeclaringTypeName() + "]" +
                "[" + ((MethodSignature) joinPoint.getSignature()).getMethod().getName() + "]");
    }

}
