package com.lohika.morning.ecs.domain.talk;

import com.lohika.morning.ecs.domain.event.MorningEvent;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TalkRepository extends CrudRepository<Talk, Long> {
  List<Talk> findByEvent(MorningEvent morningEvent);

  Optional<Talk> findByEventAndGoogleSheetsTimestamp(MorningEvent morningEvent, String googleSheetTimestamp);

  List<Talk> findAll();

  List<Talk> findByTitleContainingIgnoreCaseOrThesesContaining(String title, String theses);
}
