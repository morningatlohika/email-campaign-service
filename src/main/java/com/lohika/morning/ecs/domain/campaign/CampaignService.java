package com.lohika.morning.ecs.domain.campaign;

import lombok.RequiredArgsConstructor;

import com.lohika.morning.ecs.domain.event.MorningEvent;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignService {
  private final CampaignRepository campaignRepository;

  public Campaign findOne(Long id) {
    return campaignRepository.findOne(id);
  }

  public List<Campaign> findAll() {
    return campaignRepository.findAll(new Sort(
        new Sort.Order(Sort.Direction.ASC, "event.eventNumber"),
        new Sort.Order(Sort.Direction.ASC, "priority")));
  }

  public long count() {
    return campaignRepository.count();
  }

  public List<Campaign> filterBy(String value) {
    return campaignRepository.findByNameContainingIgnoringCaseOrSubjectContainingIgnoringCase(value, value);
  }

  public List<Campaign> findByEventId(MorningEvent event) {
    return campaignRepository.findByEvent(event);
  }

  public void save(Campaign campaign) {
    campaignRepository.save(campaign);
  }

  public void save(Iterable<Campaign> campaigns) {
    campaignRepository.save(campaigns);
  }

  public void delete(Campaign campaign) {
    if (!campaign.isEditable()) {
      throw new RuntimeException("Cannot delete campaign in status " + campaign.getStatus());
    }
    campaignRepository.delete(campaign);
  }

  public Campaign newCampaign() {
    return Campaign.builder()
        .build();
  }

  public Campaign updateStatus(final Campaign campaign, Campaign.Status status) {
    Campaign campaignToUpdate = campaignRepository.findOne(campaign.getId());
    campaignToUpdate.setStatus(status);
    return campaignRepository.save(campaignToUpdate);
  }

  public List<Campaign> getCampains(Campaign.Status status) {
    return campaignRepository.findByStatus(status);
  }
}
