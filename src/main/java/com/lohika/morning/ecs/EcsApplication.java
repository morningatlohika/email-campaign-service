package com.lohika.morning.ecs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcsApplication {

    public static final String SYS_PROP_SPRING_CONFIG_LOCATION = "spring.config.location";

    public static void main(String[] args) {
        if (System.getProperty(SYS_PROP_SPRING_CONFIG_LOCATION) != null) {
            System.setProperty(SYS_PROP_SPRING_CONFIG_LOCATION, System.getProperty("user.home") + "/.morning/ecs/");
        }

        SpringApplication.run(EcsApplication.class, args);
    }
}
