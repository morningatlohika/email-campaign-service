package com.lohika.morning.ecs.domain.campaigntemplate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "campaign_templates")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CampaignTemplate {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(unique = true)
  @NotEmpty
  @Builder.Default
  private String name = "";

  @NotEmpty
  @Builder.Default
  private String subject = "";

  @NotEmpty
  @Column(length = 2_000)
  @Builder.Default
  private String body = "";

  @Builder.Default
  private Integer priority = 0;

  @Builder.Default
  private boolean attendee = false;
  @Builder.Default
  private String emails = "";

  public String getEmails() {
    return attendee ? "All attendees" : emails;
  }
}
