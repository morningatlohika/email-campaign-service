package com.lohika.morning.ecs.domain.campaigntemplate;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CampaignTemplateRepository extends PagingAndSortingRepository<CampaignTemplate, Long> {
  List<CampaignTemplate> findAll(Sort sort);

  List<CampaignTemplate> findByNameContainingIgnoringCaseOrSubjectContainingIgnoringCase(String email, String subject);
}
