package com.lohika.morning.ecs.domain.campaign;

import lombok.RequiredArgsConstructor;

import com.lohika.morning.ecs.domain.campaigntemplate.CampaignTemplate;
import com.lohika.morning.ecs.domain.campaigntemplate.CampaignTemplateService;
import com.lohika.morning.ecs.domain.event.MorningEvent;

import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class AutoCampaignService {
  private final CampaignService campaignService;
  private final CampaignTemplateService campaignTemplateService;

  public void autoProvisionCampaigns(MorningEvent event) {
    if (!isEmpty(campaignService.findByEventId(event))) {
      throw new RuntimeException("Can not perform auto provision. Some campaign(s) exist.");
    }

    List<CampaignTemplate> campaignTemplates = campaignTemplateService.findAll();

    List<Campaign> campaigns = campaignTemplates.stream()
        .filter(CampaignTemplate::isReady)
        .map(template -> new Campaign(event, template))
        .collect(toList());

    campaignService.save(campaigns);
  }
}
