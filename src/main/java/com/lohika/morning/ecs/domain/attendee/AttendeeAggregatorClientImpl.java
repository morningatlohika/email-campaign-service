package com.lohika.morning.ecs.domain.attendee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "attendee-aggregator", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class AttendeeAggregatorClientImpl implements AttendeeAggregatorClient {
  private final AttendeeAggregatorProperties attendeeAggregatorProperties;

  @Override
  public List<Attendee> load() {
    if (!attendeeAggregatorProperties.getEnabled()) {
      log.warn("Attendee aggregator service is disable!");
      return Collections.emptyList();
    }
    return Collections.emptyList();
  }
}
