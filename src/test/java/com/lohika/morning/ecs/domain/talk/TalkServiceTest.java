package com.lohika.morning.ecs.domain.talk;

import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.lohika.morning.ecs.domain.speaker.Speaker;
import com.lohika.morning.ecs.domain.speaker.SpeakerRepository;
import com.lohika.morning.ecs.utils.TestDataGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TalkServiceTest {

  @Autowired
  private TalkRepository talkRepository;

  @Autowired
  private SpeakerRepository speakerRepository;

  @Autowired
  private TestDataGenerator dataGenerator;

  @Test
  public void getTalks() {
    // Given
    // talk with speakers
    MorningEvent event = dataGenerator.event("Test speakers").save();
    MorningEvent noiseEvent = dataGenerator.event("Noise").save();

    Talk talk = dataGenerator.talk("Test speakers talk 1")
        .withEvent(event)
        .withSpeakers(dataGenerator.speaker("sp1").build(), dataGenerator.speaker("sp2").build())
        .save();

    // When
    List<Talk> talks = talkRepository.findByEvent(event);

    // Then
    //Assert.assertThat(talks, notNullValue());
    List<Speaker> speakers = speakerRepository.findAll();
    Assert.assertThat(speakers, hasSize(2));

    Assert.assertThat(talks, hasSize(1));

    Talk savedTalk = talks.get(0);
    List<Speaker> savedTalkSpeakers = savedTalk.getSpeakers();

    Assert.assertThat(savedTalkSpeakers, hasSize(2));
    Assert.assertEquals(savedTalkSpeakers.get(0).getTalk().getId(), savedTalk.getId());
    Assert.assertEquals(savedTalkSpeakers.get(1).getTalk().getId(), savedTalk.getId());
  }
}
