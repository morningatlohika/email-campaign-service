package com.lohika.morning.ecs.domain.settings;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SettingsService {
  private final SettingsRepository settingsRepository;

  public Settings getSettings() {
    List<Settings> all = settingsRepository.findAll();

    if (all.size() > 1) {
      throw new RuntimeException("At most 1 settings record should be in DB, but " + all.size() + " found");
    }

    Optional<Settings> first = all.stream().findFirst();

    return first
        .orElseGet(() -> settingsRepository.save(Settings.builder().build()));
  }

  public void save(Settings settings) {
    settingsRepository.save(settings);
  }
}
