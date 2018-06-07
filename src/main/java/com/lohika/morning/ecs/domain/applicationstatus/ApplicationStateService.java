package com.lohika.morning.ecs.domain.applicationstatus;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ApplicationStateService {
  private final ApplicationState applicationState = ApplicationState.builder().build();

  public String getUpdateAttendee() {
    return applicationState.getLastUpdateAttendeeAt().toString();
  }

  public void updateAttendee() {
    applicationState.setLastUpdateAttendeeAt(LocalDateTime.now());
  }
}
