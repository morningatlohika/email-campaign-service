package com.lohika.morning.ecs.domain.email;

import com.lohika.morning.ecs.domain.attendee.Attendee;
import com.lohika.morning.ecs.domain.attendee.AttendeeService;
import com.lohika.morning.ecs.domain.campaign.Campaign;
import com.lohika.morning.ecs.domain.campaign.CampaignPreviewService;
import com.lohika.morning.ecs.domain.campaign.CampaignService;
import com.lohika.morning.ecs.domain.unsubscribe.Unsubscribe;
import com.lohika.morning.ecs.domain.unsubscribe.UnsubscribeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.lohika.morning.ecs.utils.EcsUtils.formatString;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final CampaignPreviewService campaignPreviewService;
  private final CampaignService campaignService;
  private final AttendeeService attendeeService;
  private final UnsubscribeService unsubscribeService;

  private final EmailRepository emailRepository;

  @Scheduled(fixedDelayString = "${email-client-service.campaign-processing-period}")
  public void preProcessCampaigns() {
    campaignService.getCampains(Campaign.Status.PENDING).forEach(campaign -> {
      try {
        campaignService.updateStatus(campaign, Campaign.Status.PREPROCESSING);
        compileEmails(campaign);
        campaignService.updateStatus(campaign, Campaign.Status.READY_TO_SEND);
      } catch (Exception e) {
        log.error(formatString("Failed to process {}/{} campaign: {}", campaign.getEvent().getName(), campaign.getName()), e);
        campaignService.updateStatus(campaign, Campaign.Status.FAILED);
      }
    });
  }

  private void compileEmails(Campaign campaign) {
    List<String> emailAdresses;
    if (campaign.isAttendee()) {
      // publish to all
      emailAdresses = attendeeService.findAll().stream().map(Attendee::getEmail).collect(toList());
    } else {
      // publish to
      emailAdresses = Arrays.asList(campaign.getEmails().split(","));
    }

    List<String> unsubscribed = unsubscribeService.findAll().stream()
        .map(Unsubscribe::getEmail)
        .map(String::toLowerCase)
        .map(String::trim)
        .collect(toList());
    emailAdresses = emailAdresses.stream().filter(e -> !unsubscribed.contains(e)).collect(toList());

    emailAdresses.forEach(e -> save(e, campaignPreviewService.findOne(campaign.getId())));
  }

  private void save(String to, Campaign campaign) {
    Email email = Email.builder()
        .to(to)
        .campaign(campaign)
        .subject(campaign.getSubject())
        .body(campaign.getBody())
        .generatedAt(LocalDate.now())
        .build();

    emailRepository.save(email);
  }

  @Deprecated
  // TODO: filter by campaign
  public List<Email> findAll() {
    return emailRepository.findAll();
  }


  public List<Email> get(Campaign campaign) {
    return emailRepository.findByCampaign(campaign);
  }

  public void delete(Email email) {
    emailRepository.delete(email);
  }

  public List<Email> filterBy(String value) {
    return emailRepository.findByToContainingIgnoreCaseOrCampaignNameContainingIgnoreCaseOrCampaignEventNameContainingIgnoreCaseOrSubjectContainingOrBodyContaining(value, value, value, value, value);
  }

  public void save(Email email) {
    emailRepository.save(email);
  }
}
