package com.lohika.morning.ecs.domain.event;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface EventRepository extends PagingAndSortingRepository<MorningEvent, Long> {
    List<MorningEvent> findAll();
    List<MorningEvent> findByNameContainsIgnoreCase(String pattern);
}
