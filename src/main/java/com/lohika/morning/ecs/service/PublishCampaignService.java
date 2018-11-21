package com.lohika.morning.ecs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

import com.lohika.morning.ecs.domain.applicationstatus.ApplicationStateService;
import com.lohika.morning.ecs.domain.campaign.Campaign;
import com.lohika.morning.ecs.domain.campaign.CampaignService;
import com.lohika.morning.ecs.domain.email.Email;
import com.lohika.morning.ecs.domain.email.Email.Status;
import com.lohika.morning.ecs.domain.email.EmailService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.lohika.morning.ecs.utils.EcsUtils.formatString;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublishCampaignService {

  private final ApplicationStateService applicationStateService;
  private final CampaignService campaignService;
  private final EmailService emailService;
  private final ExchangeService exchangeService;
  private final EmailRecipientProvider emailRecipientProvider;

  @Value("${email-client-service.recipient-email-override}")
  private String recipientEmailOverride;

  @Scheduled(fixedDelayString = "${email-client-service.campaign-processing-period}")
  public void publishAll() {
    campaignService.getCampains(Campaign.Status.READY_TO_SEND).forEach(campaign -> {
      try {
        campaignService.updateStatus(campaign, Campaign.Status.SENDING);
        publish(campaign);
        campaignService.updateStatus(campaign, Campaign.Status.SENT);
      } catch (Exception e) {
        log.error(formatString("Failed to publish {}/{} campaign: {}", campaign.getEvent().getName(), campaign.getName()), e);
        campaignService.updateStatus(campaign, Campaign.Status.FAILED);
      }
    });
    applicationStateService.updateSendEmail();
  }

  private void publish(Campaign campaign) {
    List<Email> emails = emailService.get(campaign).stream()
        .filter(email -> Status.NEW == email.getStatus())
        .collect(Collectors.toList());

    for (Email email : emails) {
      try {
        send(email);
        email.setStatus(Status.SENT);
      } catch (Exception e) {
        log.warn(formatString("Failed to send email to {} Campaign: {}/{}", email.getTo(), campaign.getEvent().getName(), campaign.getName()), e);
        email.setStatus(Status.FAILED);
      }
      email.setLastSendingAttempt(LocalDateTime.now());
      emailService.save(email);
    }
  }

  public void reSend(Email email) {
    email.setStatus(Email.Status.NEW);
    emailService.save(email);
    campaignService.updateStatus(email.getCampaign(), Campaign.Status.READY_TO_SEND);
  }

  private void send(Email email) throws Exception {
    EmailMessage msg = new EmailMessage(exchangeService);
    msg.setSubject(email.getSubject());
    msg.setBody(MessageBody.getMessageBodyFromText(email.getBody()));

    String recipientEmail = emailRecipientProvider.getRecipientEmail(email);

    msg.getToRecipients().addSmtpAddressRange(Arrays.asList(recipientEmail.split(", ")).iterator());

    if (StringUtils.isNotBlank(email.getCc())) {
      msg.getCcRecipients().add(emailRecipientProvider.getCcEmail(email));
    }

    if (StringUtils.isNotBlank(email.getBcc())) {
      msg.getBccRecipients().add(emailRecipientProvider.getBccEmail(email));
    }

    log.debug("Sending {} email. Original to: {}, final to: {}", email.getSubject(), email.getTo(), recipientEmail);
    if (!"none".equalsIgnoreCase(recipientEmailOverride)) {
      msg.send();
    } else {
      log.debug("Email sending disabled by the email-client-service.recipient-email-override property");
    }
  }

}
