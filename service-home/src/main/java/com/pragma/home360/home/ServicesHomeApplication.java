package com.pragma.home360.home;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServicesHomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicesHomeApplication.class, args);
    }

}
