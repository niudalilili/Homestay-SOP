package com.tanyde;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class HomestayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomestayServiceApplication.class, args);
        log.info("sever started");
    }

}
