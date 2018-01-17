package com.lohika.morning.ecs.domain.event;

import com.lohika.morning.ecs.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventRepositoryTest extends BaseTest {

    @Autowired
    private EventRepository repository;

    @Test
    public void getById_OK() {
        // Given
        Event event1 = given.event("Golang Morning")
                .withDescription("Another interesting event from Morning@Lohika will take place this December, 16. ")
                .withDate(LocalDate.of(2017, 12, 16))
                .save();

        Event noiseEvent = given.event("Robotic Morning")
                .withDescription("After a very short pause, we invite you to our next event dedicated to Robotics. ")
                .withDate(LocalDate.of(2017, 11, 11))
                .save();

        // When
        Event foundEvent = repository.findOne(event1.getId());

        // Then
        assertEquals(2, repository.count());

        assertEquals(event1.getId(), foundEvent.getId());
        assertEquals(event1.getName(), foundEvent.getName());
        assertEquals(event1.getDescription(), foundEvent.getDescription());
        assertEquals(event1.getDate(), foundEvent.getDate());
    }
}