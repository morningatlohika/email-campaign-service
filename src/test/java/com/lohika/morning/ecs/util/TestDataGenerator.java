package com.lohika.morning.ecs.util;

import com.lohika.morning.ecs.domain.event.EventRepository;
import com.lohika.morning.ecs.domain.event.EventRequest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TestDataGenerator {

    @Autowired
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    private EventRepository eventRepository;

    public static EventRequest eventRequest(String name) {
        return EventRequest.builder().name(name).description(name + " description").date(LocalDate.now().toString()).build();
    }

    public EventDataBuilder event(String name) {
        return new EventDataBuilder(eventRepository).event(name);
    }


    @SneakyThrows
    public String asJsonString(Object o) {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        messageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}