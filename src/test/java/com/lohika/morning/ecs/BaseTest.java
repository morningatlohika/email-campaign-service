package com.lohika.morning.ecs;

import com.lohika.morning.ecs.domain.event.EventRepository;
import com.lohika.morning.ecs.domain.speaker.SpeakerRepository;
import com.lohika.morning.ecs.domain.talk.TalkRepository;
import com.lohika.morning.ecs.utils.TestDataGenerator;

import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class BaseTest {

  @Autowired
  protected TestDataGenerator given;

  @Autowired
  protected EventRepository eventRepository;

  @Autowired
  protected TalkRepository talkRepository;

  @Autowired
  protected SpeakerRepository speakerRepository;

  @After
  public void cleanUp() {
    speakerRepository.deleteAll();
    talkRepository.deleteAll();
    eventRepository.deleteAll();
  }
}