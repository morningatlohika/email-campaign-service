package com.lohika.morning.ecs;

import lombok.extern.slf4j.Slf4j;

import com.lohika.morning.ecs.domain.attendee.AttendeeAggregatorProperties;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class RestTemplateConfiguration {

  @Bean
  public RestTemplate attendeeAggregatorRestTemplate(final RestTemplateBuilder restTemplateBuilder,
                                                     final AttendeeAggregatorProperties attendeeAggregatorProperties) {
    return restTemplateBuilder
        .basicAuthorization(attendeeAggregatorProperties.getUsername(), attendeeAggregatorProperties.getPassword())
        .build();
  }

}
