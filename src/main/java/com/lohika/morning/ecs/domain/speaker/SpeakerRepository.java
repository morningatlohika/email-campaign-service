package com.lohika.morning.ecs.domain.speaker;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeakerRepository extends CrudRepository<Speaker, Long> {
  List<Speaker> findAll();

  List<Speaker> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
}
