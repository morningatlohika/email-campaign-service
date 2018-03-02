package com.lohika.morning.ecs.utils;

import com.lohika.morning.ecs.domain.event.MorningEvent;
import org.slf4j.helpers.MessageFormatter;

import java.time.LocalDate;

public abstract class EcsUtils {
    private EcsUtils() {
        // shoudn't be instantiated
    }

    public static String formatString(String pattern, Object ... args) {
        return MessageFormatter.arrayFormat(pattern, args).getMessage();
    }

    public static boolean isEditable(MorningEvent event) {
        return event.getDate().isAfter(LocalDate.now());
    }
}
