package com.lohika.morning.ecs.domain.campaign;

import com.lohika.morning.ecs.domain.event.MorningEvent;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CampaignRepository extends PagingAndSortingRepository<Campaign, Long> {
  List<Campaign> findAll(Sort sort);

  List<Campaign> findByNameContainingIgnoringCaseOrSubjectContainingIgnoringCase(String email, String subject);

  List<Campaign> findByEvent(MorningEvent event);
}
