package com.lohika.morning.ecs.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    /**
     * There are other methods provided by Spring to get entity by ID, but they do not fit our expectations primarily
     * because we want to have a method which returns {@link Optional}
     */
    Optional<Event> findById(Long id);
}
