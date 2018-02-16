package com.lohika.morning.ecs.domain.attendee;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AttendeeRepository extends PagingAndSortingRepository<Attendee, Long>, AttendeeRepositoryCustom {
  List<Attendee> findAll();
}
