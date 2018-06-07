package com.lohika.morning.ecs.domain.applicationstatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ApplicationState {

  @Column(nullable = false)
  @NotNull
  @Builder.Default
  private LocalDateTime lastUpdateAttendeeAt = LocalDateTime.now();
}
