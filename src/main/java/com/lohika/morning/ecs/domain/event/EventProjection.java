package com.lohika.morning.ecs.domain.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "default", types = { Event.class })
public interface EventProjection {
    String getName();

    String getDescription();

    @Value("#{target.date.toString()}")
    String getDate();
}
