package com.lohika.morning.ecs.domain.campaign;

import lombok.RequiredArgsConstructor;

import com.lohika.morning.ecs.domain.event.MorningEvent;

import org.antlr.stringtemplate.StringTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CampaignPreviewService {

  private final CampaignService campaignService;

  public Campaign findOne(Long id) {
    Campaign campaign = campaignService.findOne(id);

    Map<String, String> variable = getVariable(campaign.getEvent());

    campaign.setSubject(processor(campaign.getSubject(), variable));
    campaign.setBody(processor(campaign.getBody(), variable));

    return campaign;
  }

  private HashMap<String, String> getVariable(MorningEvent event) {
    HashMap<String, String> variable = new HashMap<>();
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
