package com.lohika.morning.ecs.domain.event;

import com.lohika.morning.ecs.BaseTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventRepositoryTest extends BaseTest {

  @Test
  public void getById_OK() {
    // Given
    MorningEvent event1 = given.event("Golang Morning")
        .withDescription("Another interesting event from Morning@Lohika will take place this December, 16. ")
        .save();

    MorningEvent noiseEvent = given.event("Robotic Morning")
        .withDescription("After a very short pause, we invite you to our next event dedicated to Robotics. ")
        .save();

    // When
    MorningEvent foundEvent = eventRepository.findOne(event1.getId());

    // Then
    assertEquals(2, eventRepository.count());

    assertEquals(event1.getId(), foundEvent.getId());
    assertEquals(event1.getName(), foundEvent.getName());
    assertEquals(event1.getDescription(), foundEvent.getDescription());
    assertEquals(event1.getDate(), foundEvent.getDate());
  }

  @Test
  public void getAll_empty() {
    // Given

    // When
    List<MorningEvent> all = eventRepository.findAll();

    // Then
    assertEquals(0, eventRepository.count());
    assertEquals(0, all.size());
  }

  @Test
  public void findByDate_empty() {
    // Given
    MorningEvent event1 = given.event("Golang Morning")
        .withDescription("Another interesting event from Morning@Lohika will take place this December, 16. ")
        .save();

    // When
    MorningEvent all = eventRepository.findByDate(LocalDate.now());

    // Then
    assertEquals(1, eventRepository.count());
    assertNull(all);
  }
}