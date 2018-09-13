package com.lohika.morning.ecs.domain.campaign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.lohika.morning.ecs.domain.campaigntemplate.CampaignTemplate;
import com.lohika.morning.ecs.domain.email.Email;
import com.lohika.morning.ecs.domain.event.MorningEvent;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "campaigns",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"event_id", "name"},
            name = "uk_campaigns_event_name"
        )})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Campaign {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @NotNull
  private MorningEvent event;

  @ManyToOne
  private CampaignTemplate campaignTemplate;

  @NotEmpty
  @Builder.Default
  private String name = "";

  @NotEmpty
  @Builder.Default
  private String subject = "";

  @NotEmpty
  @Column(length = Email.EMAIL_BODY_MAX_LENGTH)
  @Builder.Default
  private String body = "";

  @Builder.Default
  private Integer priority = 0;

  @Builder.Default
  private boolean attendee = false;

  @Builder.Default
  private String emails = "";

  @Builder.Default
  private String carbonCopy = "";

  @Builder.Default
  private String blindCarbonCopy = "";

  @Builder.Default
  private String promoCode = "";

  @NotNull
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private Status status = Status.NEW;

  public Campaign(MorningEvent event, CampaignTemplate campaignTemplate) {
    this.event = event;
    this.campaignTemplate = campaignTemplate;

    name = campaignTemplate.getName();
    subject = campaignTemplate.getSubject();
    body = campaignTemplate.getBody();
    priority = campaignTemplate.getPriority();
    attendee = campaignTemplate.isAttendee();
    emails = campaignTemplate.getEmails();
    carbonCopy = campaignTemplate.getCarbonCopy();
    blindCarbonCopy = campaignTemplate.getBlindCarbonCopy();

    status = Status.NEW;
  }

  @Transient
  public boolean isEditable() {
    return Status.NEW.equals(this.status);
  }

  public String getEmails() {
    return attendee ? "All attendees" : emails;
  }

  public enum Status {
    NEW,
    PENDING,
    PREPROCESSING,
    READY_TO_SEND,
    SENDING,
    SENT,
    FAILED
  }
}
