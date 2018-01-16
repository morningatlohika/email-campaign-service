package com.lohika.morning.ecs.domain.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@NoArgsConstructor
@Getter
@Relation(value = "event", collectionRelation = "events")
public class EventResource extends ResourceSupport {
    private String name;

    private String description;

    private String date;

    public EventResource(Event event) {
        this.name = event.getName();
        this.description = event.getDescription();
        this.date = event.getDate().toString();

        add(linkTo(methodOn(EventController.class).getEvent(event.getId())).withSelfRel());
    }
}
