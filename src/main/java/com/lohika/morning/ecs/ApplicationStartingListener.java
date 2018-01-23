package com.lohika.morning.ecs;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

import java.io.File;
import java.nio.file.Paths;

import static org.springframework.boot.context.config.ConfigFileApplicationListener.CONFIG_LOCATION_PROPERTY;

@Slf4j
public class ApplicationStartingListener implements ApplicationListener<ApplicationStartingEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
        setSpringConfigLocation();
    }

    private static void setSpringConfigLocation() {
        if (System.getProperty(CONFIG_LOCATION_PROPERTY) == null) {
            log.info("{} not specified. Setting default value...", CONFIG_LOCATION_PROPERTY);

            System.setProperty(CONFIG_LOCATION_PROPERTY,
                    Paths.get(SystemUtils.USER_HOME, ".morning", "ecs").toString() + File.separator);
        }
        log.info("{} = {}", CONFIG_LOCATION_PROPERTY, System.getProperty(CONFIG_LOCATION_PROPERTY));
    }
}
