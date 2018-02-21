package com.lohika.morning.ecs.domain.attendee;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AttendeeRepository extends PagingAndSortingRepository<Attendee, Long> {
  List<Attendee> findAll();

  List<Attendee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String firstName, String lastName, String email);
}
