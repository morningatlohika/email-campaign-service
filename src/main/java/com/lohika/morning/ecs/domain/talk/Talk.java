package com.lohika.morning.ecs.domain.talk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.lohika.morning.ecs.domain.speaker.Speaker;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
  private MorningEvent event;

  @Column(unique = true)
  @NotEmpty
  private String title;

  @Column(length = 5000)
  @NotEmpty
  private String theses;

  /**
   * Used during re-import to identify talks to update
   */
  @Column(unique = true)
  @NotNull
  private String googleSheetsTimestamp;

  @OneToMany(mappedBy = "talk", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Speaker> speakers;
}
