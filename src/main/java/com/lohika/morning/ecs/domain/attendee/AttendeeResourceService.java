package com.lohika.morning.ecs.domain.attendee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.lohika.morning.ecs.domain.applicationstatus.ApplicationStateService;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AttendeeResourceService {
  private final AttendeeService attendeeService;
  private final ApplicationStateService applicationStateService;

  public Resource getResource() {
    return new StreamResource((StreamResource.StreamSource) () -> {
      List<Attendee> attendees = attendeeService.findAll();

      String mail = attendees.stream()
          .map(Attendee::getEmail)
          .collect(Collectors.joining("\n"));

      log.info("Downloaded {} attendee(s)", attendees.size());
      return new ByteArrayInputStream(mail.getBytes());
    }, "Morning@Lohika " + applicationStateService.getUpdateAttendee() + ".emails");
  }
}
