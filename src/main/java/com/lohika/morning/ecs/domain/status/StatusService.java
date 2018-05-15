package com.lohika.morning.ecs.domain.status;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatusService {
  private final StatusRepository statusRepository;

  public Status getStatus() {
    List<Status> all = statusRepository.findAll();

    if (all.size() > 1) {
      throw new RuntimeException("At most 1 status record should be in DB, but " + all.size() + " found");
    }

    Optional<Status> first = all.stream().findFirst();

    return first
        .orElseGet(() -> statusRepository.save(Status.builder().build()));
  }

  public void updateAttendee() {
    Status status = getStatus();
    status.setLastUpdateAttendeeAt(LocalDateTime.now());
    statusRepository.save(status);
  }
}
