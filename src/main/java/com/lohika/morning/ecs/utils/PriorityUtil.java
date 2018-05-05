package com.lohika.morning.ecs.utils;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.abs;
import static java.util.stream.Collectors.toList;

@UtilityClass
public class PriorityUtil {

  public static final int MIN_PRIORITY = -25;
  public static final int MAX_PRIORITY = 15;

  public static List<Integer> getPriorities() {
    return IntStream.range(MIN_PRIORITY, MAX_PRIORITY).boxed().collect(toList());
  }
  
  public static String generatePriorityCaption(Integer i) {
    if (i == 0) {
      return "in event day";
    } else if (i % 7 == 0) {
      return abs(i / 7) + " week(s) " + ((i > 0) ? "after" : "before");
    }
    return abs(i) + " day(s) " + ((i > 0) ? "after" : "before");
  }
}
