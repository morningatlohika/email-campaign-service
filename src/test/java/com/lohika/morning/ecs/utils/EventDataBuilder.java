package com.lohika.morning.ecs.utils;

import com.lohika.morning.ecs.domain.event.EventRepository;
import com.lohika.morning.ecs.domain.event.MorningEvent;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class EventDataBuilder {
  private final EventRepository eventRepository;

  private MorningEvent event;
  private boolean idSet;

  public EventDataBuilder(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  /**
   * Create {@link MorningEvent}.
   */
  EventDataBuilder event(String name) {
    this.event = MorningEvent.builder()
            .eventNumber(RandomUtils.nextInt(1, 200))
            .name(name)
            .description(name + " description")
            .date(LocalDate.now().plus(RandomUtils.nextInt(5, 20), ChronoUnit.DAYS))
            .ticketsUrl("http://tickets.example.com/test")
            .build();
    return this;
  }

  public EventDataBuilder withId(long id) {
    this.event.setId(id);
    this.idSet = true;
    return this;
  }

  public EventDataBuilder withEventNumber(int eventNumber) {
    this.event.setEventNumber(eventNumber);
    return this;
  }

  public EventDataBuilder withDescription(String description) {
    this.event.setDescription(description);
    return this;
  }

  public EventDataBuilder withDate(LocalDate date) {
    this.event.setDate(date);
    return this;
  }

  /**
   * Build {@link MorningEvent}.
   *
   * @return MorningEvent
   */
  public MorningEvent build() {
    return this.event;
  }

  /**
   * Persist {@link MorningEvent}
   *
   * @return MorningEvent
   */
  public MorningEvent save() {
    if (idSet) {
      throw new IllegalStateException("Cannot create MorningEvent with predefined id. Did you call withId() accidentally?");
    }

    return eventRepository.save(this.event);
  }

}
