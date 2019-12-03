package com.lohika.morning.ecs.domain.campaign;

import lombok.RequiredArgsConstructor;

import com.lohika.morning.ecs.domain.settings.SettingsService;
import com.lohika.morning.ecs.domain.talk.Talk;
import com.lohika.morning.ecs.domain.talk.TalkService;

import org.antlr.stringtemplate.StringTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.codehaus.groovy.runtime.InvokerHelper.asList;

import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class CampaignPreviewService {

  private final CampaignService campaignService;
  private final TalkService talkService;
  private final SettingsService settingsService;
  private final VariableService variableService;


  public Campaign findOne(Long id) {
    Campaign campaign = campaignService.findOne(id);
    List<Talk> talks = talkService.getTalks(campaign.getEvent());

    Map<String, String> variable = variableService.getVariable(campaign, campaign.getEvent(), talks);

    campaign.setEmails(processor(campaign.getEmails(), variable));
    campaign.setSubject(processor(settingsService.getSettings().getEmailPrefix() + campaign.getSubject(), variable));

    String body = processor(campaign.getBody() + settingsService.getSettings().getSignature(), variable);
    campaign.setBody(body);

    return campaign;
  }

  public Set<String> getUnusedPlaceholders(Long id) {
    Campaign campaign = campaignService.findOne(id);
    List<Talk> talks = talkService.getTalks(campaign.getEvent());

    Map<String, String> variable = variableService.getVariable(campaign, campaign.getEvent(), talks);

    String body = campaign.getBody();
    String[] strings = body.split("\\$");

    AtomicInteger index = new AtomicInteger(0);
    List<String> list = new ArrayList(asList(strings));
    return list.stream()
        .filter(p -> index.incrementAndGet() % 2 == 0)
        .filter(key -> variable.get(key) == null)
        .collect(toSet());
  }

  private String processor(String template, Map<String, String> variable) {
    StringTemplate stringTemplate = new StringTemplate(template);
    stringTemplate.setAttributes(variable);
    return stringTemplate.toString();
  }
}
