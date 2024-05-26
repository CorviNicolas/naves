package com.w2m.naves;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NavesApplication {

    public static void main(String[] args) {
        SpringApplication.run(NavesApplication.class, args);
    }

}
