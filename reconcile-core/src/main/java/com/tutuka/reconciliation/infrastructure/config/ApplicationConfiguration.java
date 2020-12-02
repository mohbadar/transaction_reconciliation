package com.tutuka.reconciliation.infrastructure.config;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Component
public class ApplicationConfiguration {

    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));   // It will set UTC timezone
    }


}
