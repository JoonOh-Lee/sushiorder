package com.joonoh.sushiorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRetry
@EnableScheduling
@SpringBootApplication
@EnableJpaAuditing
public class SushiorderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SushiorderApplication.class, args);
    }

}
