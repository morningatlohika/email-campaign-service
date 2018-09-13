package com.lohika.morning.ecs.domain.talk;

import com.lohika.morning.ecs.BaseTest;
import com.lohika.morning.ecs.domain.event.MorningEvent;

import com.lohika.morning.ecs.domain.speaker.Speaker;
import com.lohika.morning.ecs.domain.speaker.SpeakerRepository;
import com.lohika.morning.ecs.utils.TestDataGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TalkRepositoryTest extends BaseTest {

  @Autowired
  private TalkRepository talkRepository;

  @Autowired
  private SpeakerRepository speakerRepository;

  @Autowired
  private TestDataGenerator dataGenerator;

  @Test
  public void getById_OK() {
    // Given
    MorningEvent event1 = given.event("Golang Morning").save();
    MorningEvent event2 = given.event("Design Morning").save();

    Talk expectedTalk = given.talk("Red is not blue").withTargetAudience("JSE").withEvent(event1).save();

    Talk noiseTalk1 = given.talk("Blue is not green").withEvent(event1).save();
    Talk noiseTalk2 = given.talk("Green is not red").withEvent(event2).save();

    // When
    Talk foundTalk = talkRepository.findOne(expectedTalk.getId());

    // Then
    assertEquals(3, talkRepository.count());

    assertEquals(expectedTalk.getId(), foundTalk.getId());
    assertEquals(expectedTalk.getTitle(), foundTalk.getTitle());
    assertEquals(expectedTalk.getTheses(), foundTalk.getTheses());
    assertEquals(expectedTalk.getLanguage(), foundTalk.getLanguage());
    assertEquals(expectedTalk.getLevel(), foundTalk.getLevel());
    assertEquals(expectedTalk.getTargetAudience(), foundTalk.getTargetAudience());
    assertEquals(expectedTalk.getEvent().getId(), foundTalk.getEvent().getId());
  }


  @Test
  public void getTalks() {
    // Given
    // talk with speakers
    MorningEvent event = dataGenerator.event("Test speakers").save();
    MorningEvent noiseEvent = dataGenerator.event("Noise").save();

    dataGenerator.talk("Test speakers talk 1")
        .withEvent(event)
        .withSpeakers(dataGenerator.speaker("sp1").build(), dataGenerator.speaker("sp2").build())
        .save();

    // When
    List<Talk> talks = talkRepository.findByEvent(event);

    // Then
    List<Speaker> speakers = speakerRepository.findAll();
    Assert.assertThat(speakers, hasSize(2));

    Assert.assertThat(talks, hasSize(1));

    Talk savedTalk = talks.get(0);
    List<Speaker> savedTalkSpeakers = savedTalk.getSpeakers();

    Assert.assertThat(savedTalkSpeakers, hasSize(2));
    Assert.assertEquals(savedTalkSpeakers.get(0).getTalk().getId(), savedTalk.getId());
    Assert.assertEquals(savedTalkSpeakers.get(1).getTalk().getId(), savedTalk.getId());
  }


  @Test
  public void deleteTalks() {
    // Given
    // talk with speakers
    MorningEvent event = dataGenerator.event("Test cleanup").save();
    MorningEvent noiseEvent = dataGenerator.event("Noise").save();

    Talk talk1 = dataGenerator.talk("Test speakers talk 1")
        .withEvent(event)
        .withSpeakers(dataGenerator.speaker("sp11").build(), dataGenerator.speaker("sp12").build())
        .save();

    Talk talk2 = dataGenerator.talk("Test speakers talk 2")
        .withEvent(event)
        .withSpeakers(dataGenerator.speaker("sp21").build())
        .save();

    Talk noiseTalk = dataGenerator.talk("Noise talk")
        .withEvent(event)
        .withSpeakers(dataGenerator.speaker("noiseSp").build())
        .save();

    // When
    talkRepository.delete(Arrays.asList(talk1, talk2));

    // Then
    List<Talk> allTalks = talkRepository.findAll();

    Assert.assertThat(allTalks, hasSize(1));
    Talk remainingTalk = allTalks.get(0);

    Assert.assertEquals(remainingTalk.getId(), noiseTalk.getId());

    List<Speaker> allSpeakers = speakerRepository.findAll();
    Assert.assertThat(allSpeakers, hasSize(1));

    Assert.assertEquals(allSpeakers.get(0).getTalk().getId(), noiseTalk.getId());
    Assert.assertEquals(allSpeakers.get(0).getFirstName(), "noiseSp");
  }
}