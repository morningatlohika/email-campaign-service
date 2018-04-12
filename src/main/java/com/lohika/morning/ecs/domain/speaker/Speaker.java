package com.lohika.morning.ecs.domain.speaker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.lohika.morning.ecs.domain.talk.Talk;
import com.lohika.morning.ecs.utils.EcsUtils;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "speakers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Speaker {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotEmpty
  private String firstName;

  @NotEmpty
  private String lastName;

  @NotEmpty
  private String company;

  private String position;

  private String webProfileUrl;

  @NotEmpty
  @Column(length = 1000)
  private String about;

  @NotEmpty
  @Column
  private String photoUrl;

  @ManyToOne
  @JoinColumn(name = "talkId")
  private Talk talk;

  public String getFullName() {
    return EcsUtils.formatString("{} {}", firstName, lastName);
  }
}
