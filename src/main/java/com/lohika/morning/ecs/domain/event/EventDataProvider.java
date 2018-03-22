package com.lohika.morning.ecs.domain.event;

import lombok.RequiredArgsConstructor;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;

import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EventDataProvider extends AbstractBackEndDataProvider<MorningEvent, String> {

  private final EventService eventService;

  @Override
  protected Stream<MorningEvent> fetchFromBackEnd(Query<MorningEvent, String> query) {
    return eventService.findAll().stream();
  }

  @Override
  protected int sizeInBackEnd(Query<MorningEvent, String> query) {
    return eventService.findAll().size();
  }

  @Override
  public Object getId(MorningEvent item) {
    return item.getId();
  }


}
