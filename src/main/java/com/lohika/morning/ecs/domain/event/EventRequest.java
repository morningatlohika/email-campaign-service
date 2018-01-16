package com.lohika.morning.ecs.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class EventRequest {
    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String date;

    public Event toEntity() {
        return Event.builder()
                .name(name)
                .description(description)
                .date(LocalDate.parse(date))
                .build();
    }
}
