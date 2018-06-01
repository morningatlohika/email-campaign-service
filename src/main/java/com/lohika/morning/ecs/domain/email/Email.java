package com.lohika.morning.ecs.domain.email;

import com.lohika.morning.ecs.domain.campaign.Campaign;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
  public static final int EMAIL_BODY_MAX_LENGTH = 7_000;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne
  @NotNull
  private Campaign campaign;

  @Column(nullable = false, name = "to_email")
  @NonNull
  private String to;

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
