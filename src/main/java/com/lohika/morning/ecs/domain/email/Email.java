package com.lohika.morning.ecs.domain.email;

import com.lohika.morning.ecs.domain.campaign.Campaign;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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

  @Column(nullable = false)
  @NonNull
  private String body;

  @Column
  private boolean sent;

  @Column(nullable = false)
  private LocalDate generatedAt;

  @Column
  private LocalDate sentAt;

}
