package com.lohika.morning.ecs.utils;

import org.slf4j.helpers.MessageFormatter;

public abstract class EcsUtils {
  private EcsUtils() {
    // shoudn't be instantiated
  }

  public static String formatString(String pattern, Object... args) {
    return MessageFormatter.arrayFormat(pattern, args).getMessage();
  }
}
