package com.lohika.morning.ecs.domain.event;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends PagingAndSortingRepository<MorningEvent, Long> {
  List<MorningEvent> findAll();

  List<MorningEvent> findByNameContainsIgnoreCase(String pattern);

  @Query("SELECT coalesce(max(e.eventNumber), 0) FROM MorningEvent e")
  Integer findMaxEventNumber();

  MorningEvent findByEventNumber(int eventNumber);

  List<MorningEvent> findByDate(LocalDate date);
}
