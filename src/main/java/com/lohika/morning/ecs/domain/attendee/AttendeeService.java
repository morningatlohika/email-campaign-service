package com.lohika.morning.ecs.domain.attendee;

import lombok.RequiredArgsConstructor;

import com.lohika.morning.ecs.domain.status.StatusService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendeeService {
  private final AttendeeRepository attendeeRepository;
  private final AttendeeAggregatorClient attendeeAggregatorClient;
  private final StatusService statusService;

  public void reload() {
    attendeeRepository.deleteAll();
    List<Attendee> collect = attendeeAggregatorClient.load();
    attendeeRepository.save(collect);

    statusService.updateAttendee();
  }

  public List<Attendee> findAll() {
    return attendeeRepository.findAll();
  }

  public List<Attendee> filterBy(String value) {
    return attendeeRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(value, value, value);
  }
}
