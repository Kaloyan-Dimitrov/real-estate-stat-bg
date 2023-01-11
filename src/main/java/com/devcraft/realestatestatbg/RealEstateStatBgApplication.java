package com.devcraft.realestatestatbg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RealEstateStatBgApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealEstateStatBgApplication.class, args);
    }

}
