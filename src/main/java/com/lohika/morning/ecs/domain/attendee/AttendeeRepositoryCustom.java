package com.lohika.morning.ecs.domain.attendee;

import java.util.List;

public interface AttendeeRepositoryCustom {
  List<Attendee> filterBy(List<String> strings);
}
