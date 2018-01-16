package com.lohika.morning.ecs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Paths;

@SpringBootApplication
@Slf4j
public class EcsApplication {

    public static final String SYS_PROP_SPRING_CONFIG_LOCATION = "spring.config.location";
    public static final String SYS_PROP_USER_HOME = "user.home";
    public static final String SYS_PROP_DERBY_SYSTEM_HOME = "derby.system.home";

    public static void main(String[] args) {
        setDefaultSystemProperties();
        SpringApplication.run(EcsApplication.class, args);
    }

    private static void setDefaultSystemProperties() {
        if (System.getProperty(SYS_PROP_SPRING_CONFIG_LOCATION) == null) {
            log.info("{} not specified. Setting default value...", SYS_PROP_SPRING_CONFIG_LOCATION);

            System.setProperty(SYS_PROP_SPRING_CONFIG_LOCATION,
                    Paths.get(System.getProperty(SYS_PROP_USER_HOME), ".morning", "ecs").toString() + File.separator);
        }
        log.info("{} = {}", SYS_PROP_SPRING_CONFIG_LOCATION, System.getProperty(SYS_PROP_SPRING_CONFIG_LOCATION));

        System.setProperty(SYS_PROP_DERBY_SYSTEM_HOME, Paths.get(System.getProperty(SYS_PROP_SPRING_CONFIG_LOCATION),"data").toString());
        log.info("{} = {}", SYS_PROP_DERBY_SYSTEM_HOME, System.getProperty(SYS_PROP_DERBY_SYSTEM_HOME));
    }
}
