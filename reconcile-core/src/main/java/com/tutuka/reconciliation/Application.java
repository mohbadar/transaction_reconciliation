package com.tutuka.reconciliation;

import com.tutuka.lib.lang.applicationname.EnableApplicationName;

import com.tutuka.lib.lang.exception.service.EnableServiceException;
import com.tutuka.reconciliation.transactioncomapare.service.FileSystemStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableApplicationName
@EnableServiceException
@ComponentScan(basePackages = {"com.*"})
@EnableSwagger2
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
@EnableRetry
@EnableTransactionManagement
public class Application extends SpringBootServletInitializer{
        public static void main(String[] args) {
                SpringApplication.run(Application.class, args);
        }

        @Bean
        CommandLineRunner init(FileSystemStorageService storageService) {
                return (args) -> {
                        storageService.deleteAll();
                        storageService.init();
                };
        }
}
