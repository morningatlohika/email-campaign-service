package com.lohika.morning.ecs.domain.speaker;

import com.lohika.morning.ecs.BaseTest;
import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.lohika.morning.ecs.domain.talk.Talk;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpeakerRepositoryTest extends BaseTest {

  @Test
  public void getById_OK() {
    // Given
    MorningEvent event1 = given.event("Golang Morning").save();

    Speaker speaker1 = given.speaker("Michael", "Brown").build();
    Speaker speaker2 = given.speaker("Alice", "White").build();
    Speaker speaker3 = given.speaker("James", "Black").build();

    Talk expectedTalk = given.talk("Red is not blue")
            .withEvent(event1)
            .withSpeakers(speaker1, speaker2)
            .save();

    Talk noiseTalk = given.talk("Green is not blue")
            .withEvent(event1)
            .withSpeakers(speaker3)
            .save();

    // When
    Talk foundTalk = talkRepository.findOne(expectedTalk.getId());

    // Then
    assertEquals(3, speakerRepository.count());

    List<Speaker> speakers = expectedTalk.getSpeakers();
    assertEquals(2, speakers.size());

    assertEquals(speaker1.getLastName(), speakers.get(0).getLastName());
    assertEquals(speaker2.getLastName(), speakers.get(1).getLastName());
  }
}