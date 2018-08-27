package com.lohika.morning.ecs.domain.campaign;

import lombok.RequiredArgsConstructor;

import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.lohika.morning.ecs.domain.speaker.Speaker;
import com.lohika.morning.ecs.domain.talk.Talk;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

@Service
@RequiredArgsConstructor
public class VariableService {

  public Map<String, String> getVariable(Campaign campaign, MorningEvent event, List<Talk> talks) {
    Map<String, String> variable = new HashMap<>();

    variable.putAll(getVariable(campaign));
    variable.putAll(getVariable(event));
    variable.putAll(getTalksVariable(talks));

    return variable;
  }

  private Map<String, String> getVariable(Campaign campaign) {
    Map<String, String> variable = new HashMap<>();

    variable.put("promo_code", campaign.getPromoCode());

    return variable;
  }

  private Map<String, String> getVariable(MorningEvent event) {
    Map<String, String> variable = new HashMap<>();

    variable.put("event_number", valueOf(event.getEventNumber()));
    variable.put("event_name", event.getName());
    variable.put("event_url", event.getTicketsUrl());
    variable.put("tickets_url", event.getTicketsUrl());
    variable.put("event_date", event.getDate().toString());
    variable.put("event_description", event.getDescription());
    variable.put("event_tickets_url", event.getTicketsUrl());

    return variable;
  }

  private Map<String, String> getTalksVariable(List<Talk> talks) {
    Map<String, String> variable = new HashMap<>();

    long count = talks.stream()
        .map(Talk::getSpeakers)
        .mapToLong(Collection::size)
        .sum();

    variable.put("count_of_speaker", String.valueOf(count));

    int i = 1;
    for (Talk talk : talks) {
      String prefixKey = "speaker_" + i++;
      variable.putAll(getTalkVariable(prefixKey, talk));
    }

    return variable;
  }

  private Map<String, String> getTalkVariable(String prefixKey, Talk talk) {
    Map<String, String> variable = new HashMap<>();

    variable.put(prefixKey + "_talk_title", talk.getTitle());
    variable.put(prefixKey + "_talk_language", talk.getLanguage().toString());
    variable.put(prefixKey + "_talk_description", talk.getTheses());
    variable.put(prefixKey + "_level", talk.getLevel().toString());

    variable.putAll(getSpeakersVariable(prefixKey, talk));

    return variable;
  }

  private Map<String, String> getSpeakersVariable(String prefixKey, Talk talk) {
    Map<String, String> variable = new HashMap<>();

    int i = 1;
    String speakerPrefixKey = prefixKey;
    for (Speaker speaker : talk.getSpeakers()) {
      variable.putAll(getSpeakerVariable(speakerPrefixKey, speaker));
      speakerPrefixKey = speakerPrefixKey + "_" + i++;
    }

    return variable;
  }

  private Map<String, String> getSpeakerVariable(String prefixKey, Speaker speaker) {
    Map<String, String> variable = new HashMap<>();

    variable.put(prefixKey + "_name", speaker.getFullName());
    variable.put(prefixKey + "_company", speaker.getCompany());
    variable.put(prefixKey + "_position", speaker.getPosition());
    variable.put(prefixKey + "_live_in", speaker.getCity());
    variable.put(prefixKey + "_about", speaker.getAbout());
    variable.put(prefixKey + "_profile", speaker.getWebProfileUrl());
    variable.put(prefixKey + "_photo", speaker.getPhotoUrl());

    return variable;
  }
}
