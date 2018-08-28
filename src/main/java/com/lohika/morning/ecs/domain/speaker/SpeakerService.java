package com.lohika.morning.ecs.domain.speaker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpeakerService {

  private final SpeakerRepository speakerRepository;

  public List<Speaker> findAll() {
    return speakerRepository.findAll();
  }

  public List<Speaker> filterBy(String value) {
    return speakerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(value, value);
  }
}
