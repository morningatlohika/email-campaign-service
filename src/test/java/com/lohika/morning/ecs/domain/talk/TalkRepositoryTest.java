package com.lohika.morning.ecs.domain.talk;

import com.lohika.morning.ecs.BaseTest;
import com.lohika.morning.ecs.domain.event.MorningEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TalkRepositoryTest extends BaseTest {

  @Test
  public void getById_OK() {
    // Given
    MorningEvent event1 = given.event("Golang Morning").save();
    MorningEvent event2 = given.event("Design Morning").save();

    Talk expectedTalk = given.talk("Red is not blue").withEvent(event1).save();

    Talk noiseTalk1 = given.talk("Blue is not green").withEvent(event1).save();
    Talk noiseTalk2 = given.talk("Green is not red").withEvent(event2).save();

    // When
    Talk foundTalk = talkRepository.findOne(expectedTalk.getId());

    // Then
    assertEquals(3, talkRepository.count());

    assertEquals(expectedTalk.getId(), foundTalk.getId());
    assertEquals(expectedTalk.getTitle(), foundTalk.getTitle());
    assertEquals(expectedTalk.getTheses(), foundTalk.getTheses());
    assertEquals(expectedTalk.getEvent().getId(), foundTalk.getEvent().getId());
  }
}