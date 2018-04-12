package com.lohika.morning.ecs.utils;

import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.lohika.morning.ecs.domain.speaker.Speaker;
import com.lohika.morning.ecs.domain.talk.Talk;
import com.lohika.morning.ecs.domain.talk.TalkRepository;

import org.apache.commons.lang3.RandomUtils;

import java.util.Arrays;

public class TalkDataBuilder {

  private final TalkRepository talkRepository;

  private Talk talk;
  private boolean idSet;

  public TalkDataBuilder(TalkRepository talkRepository) {
    this.talkRepository = talkRepository;
  }

  /**
   * Create {@link Talk}.
   */
  TalkDataBuilder talk(String title) {
    this.talk = Talk.builder()
        .googleSheetsTimestamp(new String(RandomUtils.nextBytes(15)))
        .title(title)
        .theses(title + " theses")
        .build();
    return this;
  }

  public TalkDataBuilder withId(long id) {
    this.talk.setId(id);
    this.idSet = true;
    return this;
  }

  public TalkDataBuilder withTheses(String theses) {
    this.talk.setTheses(theses);
    return this;
  }

  public TalkDataBuilder withSpeakers(Speaker... speakers) {
    this.talk.setSpeakers(Arrays.asList(speakers));
    return this;
  }

  public TalkDataBuilder withEvent(MorningEvent event) {
    this.talk.setEvent(event);
    return this;
  }

  /**
   * Build {@link Talk}.
   *
   * @return Talk
   */
  public Talk build() {
    return this.talk;
  }

  /**
   * Persist {@link Talk}
   *
   * @return Talk
   */
  public Talk save() {
    if (idSet) {
      throw new IllegalStateException("Cannot create Talk with predefined id. Did you call withId() accidentally?");
    }

    return talkRepository.save(this.talk);
  }

}
