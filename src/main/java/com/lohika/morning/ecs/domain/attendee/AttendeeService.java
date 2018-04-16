package com.lohika.morning.ecs.domain.attendee;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendeeService {
  private final AttendeeRepository attendeeRepository;
  private final AttendeeAggregatorClient attendeeAggregatorClient;

  public void reload() {
    attendeeRepository.deleteAll();
    List<Attendee> collect = attendeeAggregatorClient.load();
    attendeeRepository.save(collect);
  }

  public List<Attendee> findAll() {
    return attendeeRepository.findAll();
  }

  public List<Attendee> filterBy(String value) {
    return attendeeRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(value, value, value);
  }
}
