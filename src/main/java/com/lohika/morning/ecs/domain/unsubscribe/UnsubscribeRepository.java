package com.lohika.morning.ecs.domain.unsubscribe;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;

public interface UnsubscribeRepository extends PagingAndSortingRepository<Unsubscribe, Long> {
  List<Unsubscribe> findAll();

  List<Unsubscribe> findByEmailContainingIgnoreCase(String email);

  List<Unsubscribe> findByEmailIn(Collection<String> emails);
}
