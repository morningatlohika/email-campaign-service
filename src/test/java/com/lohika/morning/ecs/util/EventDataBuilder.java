package com.lohika.morning.ecs.util;

import com.lohika.morning.ecs.domain.event.Event;
import com.lohika.morning.ecs.domain.event.EventRepository;

import java.time.LocalDate;

public class EventDataBuilder {

    private final EventRepository eventRepository;

    private Event event;
    private boolean idSet;

    public EventDataBuilder(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Create {@link Event}.
     */
    EventDataBuilder event(String name) {
        this.event = Event.builder()
                .name(name)
                .description(name + " description")
                .date(LocalDate.now())
                .build();
        return this;
    }

    public EventDataBuilder withId(long id) {
        this.event.setId(id);
        this.idSet = true;
        return this;
    }

    public EventDataBuilder withDescription(String description) {
        this.event.setDescription(description);
        return this;
    }

    public EventDataBuilder withDate(LocalDate date) {
        this.event.setDate(date);
        return this;
    }

    /**
     * Build {@link Event}.
     * @return Event
     */
    public Event build() {
        return this.event;
    }

    /**
     * Persist {@link Event}
     * @return Event
     */
    public Event save() {
        if (idSet) {
            throw new IllegalStateException("Cannot create Event with predefined id. Did you call withId() accidentally?");
        }

        return eventRepository.save(this.event);
    }

}
