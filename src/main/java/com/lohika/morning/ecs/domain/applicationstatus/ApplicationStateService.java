package com.lohika.morning.ecs.domain.applicationstatus;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ApplicationStateService {
  private final ApplicationState applicationState = ApplicationState.builder().build();

  public String getProcessCampaign() {
    return applicationState.getLastProcessCampaignAt().toString();
  }

  public void updateProcessCampaign() {
    applicationState.setLastProcessCampaignAt(LocalDateTime.now());
  }

  public String getSendEmail() {
    return applicationState.getLastSendEmailAt().toString();
  }

  public void updateSendEmail() {
    applicationState.setLastSendEmailAt(LocalDateTime.now());
  }

  public String getUpdateAttendee() {
    return applicationState.getLastUpdateAttendeeAt().toString();
  }

  public void updateAttendee() {
    applicationState.setLastUpdateAttendeeAt(LocalDateTime.now());
  }
}
