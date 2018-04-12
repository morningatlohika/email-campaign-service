package com.lohika.morning.ecs.utils;

import com.lohika.morning.ecs.domain.event.EventRepository;
import com.lohika.morning.ecs.domain.speaker.SpeakerRepository;
import com.lohika.morning.ecs.domain.talk.TalkRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class TestDataGenerator {

  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private TalkRepository talkRepository;

  @Autowired
  private SpeakerRepository speakerRepository;

  public EventDataBuilder event(String name) {
    return new EventDataBuilder(eventRepository).event(name);
  }

  public TalkDataBuilder talk(String title) {
    return new TalkDataBuilder(talkRepository).talk(title);
  }

  public SpeakerDataBuilder speaker(String firstName, String lastName) {
    return new SpeakerDataBuilder(speakerRepository).speaker(firstName, lastName);
  }

  public SpeakerDataBuilder speaker(String firstName) {
    return new SpeakerDataBuilder(speakerRepository).speaker(firstName, firstName + "brown");
  }
}
