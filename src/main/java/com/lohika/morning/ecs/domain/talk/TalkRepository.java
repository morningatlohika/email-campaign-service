package com.lohika.morning.ecs.domain.talk;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TalkRepository extends CrudRepository<Talk, Long> {
}
