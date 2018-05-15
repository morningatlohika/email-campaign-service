package com.lohika.morning.ecs.domain.status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "settings")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Status {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  @NotNull
  @Builder.Default
  private LocalDateTime lastUpdateAttendeeAt = LocalDateTime.now();
}
