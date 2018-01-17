package com.lohika.morning.ecs.domain.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<EventResource> createEvent(@RequestBody EventRequest request) throws Exception {
        Event event = eventService.create(request.toEntity());
        Link linkToEvent = new EventResource(event).getLink(Link.REL_SELF);
        return ResponseEntity.created(URI.create(linkToEvent.getHref())).build();
    }

    @RequestMapping(path = "/{id}", method = GET)
    public ResponseEntity<EventResource> getEvent(@PathVariable long id) {
        EventResource testCaseResource = new EventResource(eventService.getEvent(id));
        return ResponseEntity.ok(testCaseResource);
    }

    @RequestMapping(method = GET)
    public Resources<EventResource> getEvents() {
        List<EventResource> eventResources = eventService.getAllEvents().stream()
                .map(e -> new EventResource(e))
                .collect(toList());
        return new Resources<>(eventResources);
    }

}
