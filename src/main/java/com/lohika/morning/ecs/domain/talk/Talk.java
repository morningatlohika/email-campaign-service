package com.lohika.morning.ecs.domain.talk;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.lohika.morning.ecs.domain.speaker.Speaker;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Builder(builderClassName = "TalkInternalBuilder", builderMethodName = "internalBuilder")
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

  @OneToMany(mappedBy = "talk", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @Builder.Default
  @Setter(AccessLevel.NONE)
  private List<Speaker> speakers = new ArrayList<>();

  public static TalkBuilder builder() {
    return new TalkBuilder();
  }

  public static class TalkBuilder extends TalkInternalBuilder {
    @Override
    public Talk build() {
      Talk t = super.build();
      t.getSpeakers().forEach(s -> s.setTalk(t));
      return t;
    }
  }

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
    UKRAINIAN("Ukrainian"),
    ENGLISH("English"),
    RUSSIAN("Russian"),
    UNDECIDED("Undecided");

    private String name;

    Language(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public static Language fromString(String value) {
      if (StringUtils.isBlank(value)) {
        return UNDECIDED;
      }

      return valueOf(value.toUpperCase());
    }
  }

  public enum Level {
    INTRODUCTORY("Introductory"),
    REGULAR("Regular"),
    DEEP_DETAILS("Deep details"),
    UNKNOWN("Unknown");

    private String name;

    Level(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public static Level fromString(String value) {
      if (StringUtils.isBlank(value)) {
        return UNKNOWN;
      }

      return valueOf(StringUtils.replace(value, " ", "_").toUpperCase());
    }
  }
}
