package com.lohika.morning.ecs.util;

import com.lohika.morning.ecs.domain.event.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestDataGenerator {

    @Autowired
    private EventRepository eventRepository;

    public EventDataBuilder event(String name) {
        return new EventDataBuilder(eventRepository).event(name);
    }
}