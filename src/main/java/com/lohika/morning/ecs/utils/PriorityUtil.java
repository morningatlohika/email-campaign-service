package com.lohika.morning.ecs.utils;

import lombok.experimental.UtilityClass;

import static java.lang.Math.abs;

@UtilityClass
public class PriorityUtil {
  public static String generatePriorityCaption(Integer i) {
    if (i == 0) {
      return "in event day";
    } else if (i % 7 == 0) {
      return abs(i / 7) + " week(s) " + ((i > 0) ? "after" : "before");
    }
    return abs(i) + " day(s) " + ((i > 0) ? "after" : "before");
  }
}
