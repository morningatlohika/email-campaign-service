package com.lohika.morning.ecs.domain.talk;

import com.lohika.morning.ecs.domain.event.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "talks")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Talk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @NotNull
    private Event event;

    @Column(unique = true)
    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String speaker;

    @NotNull
    private String speakerCompany;

    @NotNull
    private String speakerInfo;

    @NotNull
    private String ticketsUrl;
}
