package com.lohika.morning.ecs.domain.status;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface StatusRepository extends PagingAndSortingRepository<Status, Long> {
  List<Status> findAll();
}
