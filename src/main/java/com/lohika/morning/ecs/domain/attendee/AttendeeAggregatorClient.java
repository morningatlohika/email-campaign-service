package com.lohika.morning.ecs.domain.attendee;

import java.util.List;

public interface AttendeeAggregatorClient {
  List<Attendee> load();
}
