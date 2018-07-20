package com.lohika.morning.ecs.domain.talk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.lohika.morning.ecs.domain.speaker.Speaker;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

import javax.persistence.*;
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

  @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'UNDECIDED'")
  @Enumerated(EnumType.STRING)
  @NotNull
  @Builder.Default
  private Language language = Language.UNDECIDED;

  @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'UNKNOWN'")
  @Enumerated(EnumType.STRING)
  @NotNull
  @Builder.Default
  private Level level = Level.UNKNOWN;

  @Column
  private String targetAudience;

  public enum Language {
    UKRAINIAN,
    ENGLISH,
    RUSSIAN,
    UNDECIDED;

    public static Language fromString(String value) {
      if (StringUtils.isBlank(value)) {
        return UNDECIDED;
      }

      return valueOf(value.toUpperCase());
    }
  }

  public enum Level {
    INTRODUCTORY,
    REGULAR,
    DEEP_DETAILS,
    UNKNOWN;

    public static Level fromString(String value) {
      if (StringUtils.isBlank(value)) {
        return UNKNOWN;
      }

      return valueOf(StringUtils.replace(value, " ", "_").toUpperCase());
    }
  }
}
