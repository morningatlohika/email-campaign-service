package com.lohika.morning.ecs.utils;

import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.lohika.morning.ecs.domain.speaker.Speaker;
import com.lohika.morning.ecs.domain.talk.Talk;
import com.lohika.morning.ecs.domain.talk.TalkRepository;

import org.apache.commons.lang3.RandomUtils;

import java.util.Arrays;

public class TalkDataBuilder {

  private final TalkRepository talkRepository;

  private Talk.TalkBuilder talkBuilder;
  private boolean idSet;

  public TalkDataBuilder(TalkRepository talkRepository) {
    this.talkRepository = talkRepository;
  }

  /**
   * Create {@link Talk}.
   */
  TalkDataBuilder talk(String title) {
    this.talkBuilder = Talk.builder();
        talkBuilder.googleSheetsTimestamp(new String(RandomUtils.nextBytes(15)))
        .title(title)
        .theses(title + " theses")
        .language(Talk.Language.ENGLISH)
        .level(Talk.Level.REGULAR);
    return this;
  }

  public TalkDataBuilder withId(long id) {
    this.talkBuilder.id(id);
    this.idSet = true;
    return this;
  }

  public TalkDataBuilder withTheses(String theses) {
    this.talkBuilder.theses(theses);
    return this;
  }

  public TalkDataBuilder withSpeakers(Speaker... speakers) {
    this.talkBuilder.speakers(Arrays.asList(speakers));
    return this;
  }

  public TalkDataBuilder withEvent(MorningEvent event) {
    this.talkBuilder.event(event);
    return this;
  }

  public TalkDataBuilder withTargetAudience(String targetAudience) {
    this.talkBuilder.targetAudience(targetAudience);
    return this;
  }

  /**
   * Build {@link Talk}.
   *
   * @return Talk
   */
  public Talk build() {
    return this.talkBuilder.build();
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

    return talkRepository.save(this.talkBuilder.build());
  }

}
