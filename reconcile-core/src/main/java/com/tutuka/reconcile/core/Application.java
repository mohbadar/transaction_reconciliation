package com.tutuka.reconcile.core;

import com.tutuka.lib.lang.applicationname.EnableApplicationName;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableApplicationName
@ComponentScan(basePackages = {"af.*"})
@EnableSwagger2
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
@EnableRetry
@EnableTransactionManagement
public class Application extends SpringBootServletInitializer{
        public static void main(String[] args) {
                SpringApplication.run(Application.class, args);
        }
}
