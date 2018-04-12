package com.lohika.morning.ecs.domain.campaign;

import lombok.RequiredArgsConstructor;

import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.lohika.morning.ecs.domain.settings.SettingsService;

import org.antlr.stringtemplate.StringTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CampaignPreviewService {

  private final CampaignService campaignService;
  private final SettingsService settingsService;

  public Campaign findOne(Long id) {
    Campaign campaign = campaignService.findOne(id);

    Map<String, String> variable = getVariable(campaign);

    campaign.setSubject(processor(campaign.getSubject(), variable));

    String body = processor(campaign.getBody() + settingsService.getSettings().getSignature(), variable);
    campaign.setBody(body);

    return campaign;
  }

  private HashMap<String, String> getVariable(Campaign campaign) {
    HashMap<String, String> variable = new HashMap<>();
    variable.put("promo_code", campaign.getPromoCode());

    MorningEvent event = campaign.getEvent();
    variable.put("event_name", event.getName());
    variable.put("event_description", event.getDescription());
    variable.put("event_tickets_url", event.getTicketsUrl());

    return variable;
  }

  private String processor(String template, Map<String, String> variable) {
    StringTemplate stringTemplate = new StringTemplate(template);
    stringTemplate.setAttributes(variable);
    return stringTemplate.toString();
  }
}
