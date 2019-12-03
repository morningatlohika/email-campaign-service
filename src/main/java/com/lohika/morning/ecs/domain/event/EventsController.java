package com.lohika.morning.ecs.domain.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class EventsController {

  @Autowired
  private EventRepository eventRepository;

  @GetMapping("/api/events")
  public ResponseEntity<List<MorningEvent>> events(@RequestParam(value = "date") Optional<String> date) {
    List<MorningEvent> events = date.map(d -> eventRepository.findByDate(parseDate(d))).orElseGet(eventRepository::findAll);

    if (CollectionUtils.isEmpty(events)) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(events);
  }

  private LocalDate parseDate(String dateStr) {
    if ("today".equalsIgnoreCase(dateStr)) {
      return LocalDate.now();
    }

    return LocalDate.parse(dateStr);
  }

}
