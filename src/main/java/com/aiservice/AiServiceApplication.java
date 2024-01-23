package com.aiservice;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;



@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class AiServiceApplication {
// common_workflow_submission
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("IST"));
    }
    
    public static void main(String[] args) {
        SpringApplication.run(AiServiceApplication.class, args);
    }
 
}
