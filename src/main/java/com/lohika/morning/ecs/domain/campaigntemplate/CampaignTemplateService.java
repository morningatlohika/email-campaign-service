package com.lohika.morning.ecs.domain.campaigntemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignTemplateService {
  private final CampaignTemplateRepository templateRepository;

  public CampaignTemplate findOne(Long id) {
    return templateRepository.findOne(id);
  }

  public List<CampaignTemplate> findAll() {
    return templateRepository.findAll(new Sort(new Sort.Order(Sort.Direction.ASC, "priority")));
  }

  public List<CampaignTemplate> filterBy(String value) {
    return templateRepository.findByNameContainingIgnoringCaseOrSubjectContainingIgnoringCase(value, value);
  }

  public void save(CampaignTemplate template) {
    templateRepository.save(template);
  }

  public void delete(CampaignTemplate unsubscribe) {
    templateRepository.delete(unsubscribe);
  }

  public CampaignTemplate newTemplate() {
    return CampaignTemplate.builder()
            .build();
  }
}
