package com.lohika.morning.ecs.domain.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventRepositoryTest {

    @Autowired
    private EventRepository repository;

    @Test
    public void getById_OK() {
        // Given
        Event event1 = Event.builder()
                .name("Golang Morning")
                .description("Another interesting event from Morning@Lohika will take place this December, 16. ")
                .date(LocalDate.of(2017, 12, 16))
                .build();

        Event noiseEvent = Event.builder()
                .name("Robotic Morning")
                .description("After a very short pause, we invite you to our next event dedicated to Robotics. ")
                .date(LocalDate.of(2017, 11, 11))
                .build();

        Event savedEvent1 = repository.save(event1);
        repository.save(noiseEvent);

        // When
        Event foundEvent = repository.findOne(savedEvent1.getId());

        // Then
        assertEquals(2, repository.count());

        assertEquals(savedEvent1.getId(), foundEvent.getId());
        assertEquals(savedEvent1.getName(), foundEvent.getName());
        assertEquals(savedEvent1.getDescription(), foundEvent.getDescription());
        assertEquals(savedEvent1.getDate(), foundEvent.getDate());
    }
}