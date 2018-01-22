package com.lohika.morning.ecs.domain.event;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(excerptProjection = EventProjection.class)
public interface EventRepository extends PagingAndSortingRepository<Event, Long> {
    List<Event> findAll();
    List<Event> findByNameContainsIgnoreCase(String pattern);
}
