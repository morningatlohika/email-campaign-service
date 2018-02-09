package com.lohika.morning.ecs.domain.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class EventService {
    @Autowired
    private EventRepository repository;

    public MorningEvent newEvent() {
        return MorningEvent.builder()
                .eventNumber(repository.findMaxEventNumber() + 1)
                // set event date 2 weeks ahead by default
                .date(LocalDate.now().plus(2, ChronoUnit.WEEKS))
                .build();
    }

    public MorningEvent getEvent(Long id) {
        return repository.findOne(id);
    }

    public void save(MorningEvent morningEvent) {
        repository.save(morningEvent);
    }

    public void delete(MorningEvent morningEvent) {
        repository.delete(morningEvent);
    }
}
