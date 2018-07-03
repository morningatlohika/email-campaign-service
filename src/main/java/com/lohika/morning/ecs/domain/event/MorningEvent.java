package com.lohika.morning.ecs.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.NotEmpty;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "events")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MorningEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(unique = true)
  @NotNull
  private int eventNumber;

  @Column(unique = true)
  @NotEmpty
  private String name;

  @NotEmpty
  @Column(length = 32_672)
  private String description;

  @NotNull
  @Future
  private LocalDate date;

  @Column(length = 2_000)
  private String ticketsUrl;
}
