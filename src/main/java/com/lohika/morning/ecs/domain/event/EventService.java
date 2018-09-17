package com.lohika.morning.ecs.domain.event;

import com.lohika.morning.ecs.utils.EcsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
public class EventService {
  @Autowired
  private EventRepository repository;

  public MorningEvent newEvent() {
    return MorningEvent.builder()
        .eventNumber(repository.findMaxEventNumber() + 1)
        // set event date 2 weeks ahead by default
        .date(LocalDate.now().plus(2, ChronoUnit.WEEKS))
        .build();
  }

  public List<MorningEvent> findAll() {
    return repository.findAll();
  }

  public MorningEvent getEventByNumber(int eventNumber) {
    return repository.findByEventNumber(eventNumber);
  }

  public void save(MorningEvent morningEvent) {
    repository.save(morningEvent);
  }

  public void delete(MorningEvent morningEvent) {
    repository.delete(morningEvent);
  }

  public void wrapUp(MorningEvent morningEvent) {
    morningEvent.setCompleted(true);
    repository.save(morningEvent);
  }

  public List<MorningEvent> getEvents(String filterText, boolean hidePast, boolean hideCompleted) {
    Stream<MorningEvent> eventStream = repository.findByNameContainsIgnoreCase(filterText)
        .stream();

    if (hidePast) {
        eventStream = eventStream.filter(e -> e.getDate().isAfter(LocalDate.now().minusDays(1)));
    }
    if (hideCompleted) {
        eventStream = eventStream.filter(e -> !e.isCompleted());
    }
    return eventStream.collect(toList());
  }

}
