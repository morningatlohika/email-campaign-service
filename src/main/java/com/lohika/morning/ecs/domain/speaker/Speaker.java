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

  @Column
  private String position;

  @Column(length = 500)
  private String webProfileUrl;

  @NotEmpty
  @Column(length = 32_672)
  private String about;

  @NotEmpty
  @Column(length = 500)
  private String photoUrl;

  @Column
  private String email;

  @Column
  private String city;

  @Column
  private String hotelOption;

  @Column
  private String transportTicketOption;

  @Column(length = 32_672)
  private String additionalTravelInfo;

  @Column(length = 32_672)
  private String equipmentOption;

  @ManyToOne
  @JoinColumn(name = "talkId")
  private Talk talk;

  public String getFullName() {
    return EcsUtils.formatString("{} {}", firstName, lastName);
  }
}
