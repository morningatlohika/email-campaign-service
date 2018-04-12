package com.lohika.morning.ecs.utils;

import com.lohika.morning.ecs.domain.speaker.Speaker;
import com.lohika.morning.ecs.domain.speaker.SpeakerRepository;

import static com.lohika.morning.ecs.utils.EcsUtils.formatString;

public class SpeakerDataBuilder {

  private final SpeakerRepository speakerRepository;

  private Speaker speaker;
  private boolean idSet;

  public SpeakerDataBuilder(SpeakerRepository speakerRepository) {
    this.speakerRepository = speakerRepository;
  }

  /**
   * Create {@link Speaker}.
   */
  SpeakerDataBuilder speaker(String firstName, String lastName) {
    this.speaker = Speaker.builder()
        .firstName(firstName)
        .lastName(lastName)
        .about(formatString("{} {} bio", firstName, lastName))
        .webProfileUrl(formatString("https://social.example.com/{}_{}", firstName, lastName))
        .photoUrl(formatString("https://photo.example.com/{}_{}", firstName, lastName))
        .position("Senior engineer")
        .company(formatString("{} {} LTD", firstName, lastName))
        .build();
    return this;
  }

  public SpeakerDataBuilder withId(long id) {
    this.speaker.setId(id);
    this.idSet = true;
    return this;
  }

  /**
   * Build {@link Speaker}.
   *
   * @return Speaker
   */
  public Speaker build() {
    return this.speaker;
  }

  /**
   * Persist {@link Speaker}
   *
   * @return Speaker
   */
  public Speaker save() {
    if (idSet) {
      throw new IllegalStateException("Cannot create Speaker with predefined id. Did you call withId() accidentally?");
    }

    return speakerRepository.save(this.speaker);
  }

}
