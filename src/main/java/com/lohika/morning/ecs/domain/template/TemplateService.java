package com.lohika.morning.ecs.domain.template;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class TemplateService {
  private final TemplateRepository templateRepository;

  public Template findOne(Long id) {
    return templateRepository.findOne(id);
  }

  public List<Template> findAll() {
    return templateRepository.findAll(new Sort(new Sort.Order(Sort.Direction.ASC, "priority")));
  }

  public List<Template> filterByEmail(String email) {
    return templateRepository.filterAllByName(email);
  }

  public void save(Template template) {
    templateRepository.save(template);
  }

  public void delete(Template unsubscribe) {
    templateRepository.delete(unsubscribe);
  }

  public Template newTemplate() {
    return Template.builder()
        .build();
  }
}
