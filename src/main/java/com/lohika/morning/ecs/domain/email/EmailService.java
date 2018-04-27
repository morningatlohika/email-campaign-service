package com.lohika.morning.ecs.domain.email;

import com.lohika.morning.ecs.domain.attendee.Attendee;
import com.lohika.morning.ecs.domain.attendee.AttendeeService;
import com.lohika.morning.ecs.domain.campaign.Campaign;
import com.lohika.morning.ecs.domain.campaign.CampaignPreviewService;
import com.lohika.morning.ecs.domain.campaign.CampaignService;
import com.lohika.morning.ecs.domain.unsubscribe.Unsubscribe;
import com.lohika.morning.ecs.domain.unsubscribe.UnsubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final CampaignPreviewService campaignPreviewService;
  private final CampaignService campaignService;
  private final AttendeeService attendeeService;
  private final UnsubscribeService unsubscribeService;

  private final EmailRepository emailRepository;

  public void compileEmails(Long campaignId) {
    Campaign campaign = campaignService.updateStatus(campaignId, Campaign.Status.SENDING);

    List<String> emails;
    if (campaign.isAttendee()) {
      // send to all
      emails = attendeeService.findAll().stream().map(Attendee::getEmail).collect(toList());
    } else {
      // send to
      emails = Arrays.asList(campaign.getEmails().split(","));
    }

    List<String> unsubsribed = unsubscribeService.findAll().stream().map(Unsubscribe::getEmail).collect(toList());
    emails = emails.stream().filter(e -> !unsubsribed.contains(e)).collect(toList());

    emails.forEach(e -> save(e, campaignPreviewService.findOne(campaign.getId())));
  }

  private void save(String to, Campaign campaign) {
    Email email = Email.builder()
        .to(to)
        .campaign(campaign)
        .subject(campaign.getSubject())
        .body(campaign.getBody())
        .generatedAt(LocalDate.now())
        .sent(false)
        .build();

    emailRepository.save(email);
  }

  @Deprecated
  // TODO: filter by campaign
  public List<Email> findAll() {
    return emailRepository.findAll();
  }

  public void delete(Email email) {
    emailRepository.delete(email);
  }

  public List<Email> filterBy(String value) {
    return emailRepository.findByToContainingIgnoreCaseOrCampaignNameContainingIgnoreCaseOrCampaignEventNameContainingIgnoreCaseOrSubjectContainingOrBodyContaining(value, value, value, value, value);
  }
}
