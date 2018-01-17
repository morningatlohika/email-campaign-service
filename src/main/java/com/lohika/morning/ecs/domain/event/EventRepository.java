package com.lohika.morning.ecs.domain.event;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = EventProjection.class)
public interface EventRepository extends PagingAndSortingRepository<Event, Long> {
    
}
