package com.lohika.morning.ecs.domain.settings;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SettingsRepository extends PagingAndSortingRepository<Settings, Long> {
  List<Settings> findAll();
}
