package com.lohika.morning.ecs.domain.settings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "settings")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Settings {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column()
  @Builder.Default
  private String emailPrefix = "";

  @Column(length = 2_000)
  @Builder.Default
  private String signature = "";
}
