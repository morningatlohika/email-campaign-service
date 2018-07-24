package com.lohika.morning.ecs.utils;

import com.lohika.morning.ecs.domain.campaign.Campaign;
import com.lohika.morning.ecs.domain.event.MorningEvent;

import org.slf4j.helpers.MessageFormatter;

import java.util.List;

import static com.lohika.morning.ecs.domain.campaign.Campaign.Status.NEW;

public abstract class EcsUtils {
  private EcsUtils() {
    // shoudn't be instantiated
  }

  public static String formatString(String pattern, Object... args) {
    return MessageFormatter.arrayFormat(pattern, args).getMessage();
  }

  public static boolean isEditable(MorningEvent event, List<Campaign> campaigns) {
    return campaigns.isEmpty() || campaigns.stream()
        .anyMatch(campaign -> NEW == campaign.getStatus());
  }
}
