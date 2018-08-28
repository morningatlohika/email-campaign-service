package com.lohika.morning.ecs.domain.attendee;

import lombok.RequiredArgsConstructor;

import com.lohika.morning.ecs.domain.applicationstatus.ApplicationStateService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendeeService {
  private final AttendeeRepository attendeeRepository;
  private final AttendeeAggregatorClient attendeeAggregatorClient;
  private final ApplicationStateService applicationStateService;

  public void reload() {
    attendeeRepository.deleteAll();
    List<Attendee> collect = attendeeAggregatorClient.load();
    attendeeRepository.save(collect);

    applicationStateService.updateAttendee();
  }

  public List<Attendee> findAll() {
    return attendeeRepository.findAll();
  }

  public long count() {
    return attendeeRepository.count();
  }

  public List<Attendee> filterBy(String value) {
    return attendeeRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(value, value, value);
  }
}
