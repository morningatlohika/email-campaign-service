package com.lohika.morning.ecs;

import lombok.extern.slf4j.Slf4j;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import static com.lohika.morning.ecs.utils.EcsUtils.formatString;

@Configuration
@Slf4j
public class GoogleSheetsConfiguration {

  /**
   * Global instance of the scopes required by this quickstart.
   * <p>
   * If modifying these scopes, delete your previously saved credentials
   * at ${spring.config.location}/${GOOGLE_CREDENTIALS_STORE_DIR}
   */
  private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);

  @Autowired
  private ApplicationContext applicationContext;

  @Value("${google.application-name}")
  private String applicationName;

  @Value("${spring.config.location}")
  private String springConfigLocation;

  @Bean
  @ConditionalOnProperty(value = "google.enabled", matchIfMissing = true)
  public Sheets getSheetsClient(@Autowired Environment environment) throws GeneralSecurityException, IOException {
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    File credentialsStore = new File(environment.getProperty(ApplicationEnvironmentTuner.SYS_PROP_GOOGLE_CREDS_STORE));

    Credential credentials = authorize(jsonFactory, httpTransport, credentialsStore);

    return new Sheets.Builder(httpTransport, jsonFactory, credentials)
        .setApplicationName(applicationName)
        .build();
  }

  /**
   * Creates an authorized Credential object.
   *
   * @return an authorized Credential object.
   */
  private Credential authorize(JsonFactory jsonFactory, HttpTransport httpTransport, File credentialsStore) throws IOException {
    FileDataStoreFactory dataStoreFactory = new FileDataStoreFactory(credentialsStore);

    // Load client secrets.
    InputStream in = applicationContext.getResource(formatString("file:{}/{}", springConfigLocation, "client_secret.json")).getInputStream();
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, SCOPES)
            .setDataStoreFactory(dataStoreFactory)
            .setAccessType("offline")
            .build();
    Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    log.info("Credentials saved to {}", credentialsStore.getAbsolutePath());
    return credential;
  }

}
