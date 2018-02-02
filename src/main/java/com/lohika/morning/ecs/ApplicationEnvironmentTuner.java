package com.lohika.morning.ecs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.boot.context.config.ConfigFileApplicationListener.CONFIG_LOCATION_PROPERTY;

@Slf4j
public class ApplicationEnvironmentTuner implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    public static final String SYS_PROP_GOOGLE_CREDS_STORE = "google.creds-store-dir";
    private static final String SYS_PROP_DERBY_SYSTEM_HOME = "derby.system.home";

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment env = event.getEnvironment();

        setDerbySystemHome(env);
        setGoogleCredentialsStorePath(env);
    }

    private void setDerbySystemHome(ConfigurableEnvironment env) {
        String dataDir = env.getProperty("email-client-service.data-dir");
        System.setProperty(SYS_PROP_DERBY_SYSTEM_HOME, Paths.get(System.getProperty(CONFIG_LOCATION_PROPERTY),dataDir).toString());
        log.info("{} = {}", SYS_PROP_DERBY_SYSTEM_HOME, System.getProperty(SYS_PROP_DERBY_SYSTEM_HOME));
    }

    private void setGoogleCredentialsStorePath(ConfigurableEnvironment env) {
        String googleCredsStoreDir = env.getProperty(SYS_PROP_GOOGLE_CREDS_STORE, "sheets.googleapis.morning-speakers");

        if (!Paths.get(googleCredsStoreDir).isAbsolute()) {
            Path googleCredsStoreAbsolutePath = Paths.get(env.getProperty(CONFIG_LOCATION_PROPERTY), googleCredsStoreDir);
            System.setProperty(SYS_PROP_GOOGLE_CREDS_STORE, googleCredsStoreAbsolutePath.toString());
        }
        log.info("{} = {}", SYS_PROP_GOOGLE_CREDS_STORE, System.getProperty(SYS_PROP_GOOGLE_CREDS_STORE));
    }
}
