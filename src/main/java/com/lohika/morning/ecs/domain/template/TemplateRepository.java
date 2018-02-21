package com.lohika.morning.ecs.domain.template;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TemplateRepository extends PagingAndSortingRepository<Template, Long> {
  List<Template> findAll(Sort sort);

  List<Template> findByNameContainingIgnoringCaseOrSubjectContainingIgnoringCase(String email, String subject);
}
