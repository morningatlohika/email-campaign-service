package com.lohika.morning.ecs.domain.speaker;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeakerRepository extends CrudRepository<Speaker, Long> {
}
