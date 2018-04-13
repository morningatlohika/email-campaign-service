package com.lohika.morning.ecs.domain.attendee;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Data
@Configuration
@ConfigurationProperties("attendee-aggregator")
public class AttendeeAggregatorProperties {

  @NotNull
  private Boolean enabled;

  @NotBlank
  private String url;

  @NotBlank
  private String username;

  @NotBlank
  private String password;
}
