package com.lohika.morning.ecs.domain.event;

import com.lohika.morning.ecs.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public class EventService {
    private EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event create(Event event) {
        return eventRepository.save(event);
    }

    public Event getEvent(long id) {
        return findOrThrow(id);
    }

    public Collection<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    private Event findOrThrow(long testCaseId) {
        return eventRepository.findById(testCaseId).
                orElseThrow(() -> new ResourceNotFoundException(Event.class, testCaseId));
    }
}
