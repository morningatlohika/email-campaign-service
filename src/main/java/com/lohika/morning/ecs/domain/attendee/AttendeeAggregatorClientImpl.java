package com.lohika.morning.ecs.domain.attendee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "attendee-aggregator", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class AttendeeAggregatorClientImpl implements AttendeeAggregatorClient {
  private final AttendeeAggregatorProperties attendeeAggregatorProperties;
  private final RestTemplate attendeeAggregatorRestTemplate;

  @Override
  public List<Attendee> load() {
    Object[] list = attendeeAggregatorRestTemplate.getForObject(attendeeAggregatorProperties.getUrl(), Object[].class);
    List<Object> objects = Arrays.asList(list);

    return objects.stream()
        .map(Object::toString)
        .map(email -> Attendee.builder()
            .email(email)
            .build())
        .collect(Collectors.toList());
  }
}
