package com.tutuka.reconciliation.infrastructure.config;

import com.tutuka.reconciliation.transactioncomapare.service.FileSystemStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Component
public class ApplicationConfiguration {

    /**
     * Set Default TimeZone to UTC
     */
    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    /**
     * when application initially started, it will delete fileStorage directory and its content and create the directory again
     * @param storageService
     * @return
     */
    @Bean
    CommandLineRunner init(FileSystemStorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }

}
