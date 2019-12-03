package com.lohika.morning.ecs.domain.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.lohika.morning.ecs.domain.campaign.Campaign;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "emails",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"campaign_id", "to_email"},
            name = "uk_emails_campaign_to_email"
        )})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Email {
  public static final int EMAIL_BODY_MAX_LENGTH = 32_672;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne
  @NotNull
  private Campaign campaign;

  @Column(nullable = false, name = "to_email")
  @NonNull
  private String to;

  @Builder.Default
  private String cc = "";

  @Builder.Default
  private String bcc = "";

  @Column(nullable = false)
  @NonNull
  private String subject;

  @Column(nullable = false, length = EMAIL_BODY_MAX_LENGTH)
  @NonNull
  private String body;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private Status status = Status.NEW;

  @Column(nullable = false)
  private LocalDate generatedAt;

  @Column
  private LocalDateTime lastSendingAttempt;

  public enum Status {
    NEW,
    SENT,
    FAILED
  }
}
