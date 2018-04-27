package com.lohika.morning.ecs.domain.email;


import com.lohika.morning.ecs.domain.campaign.Campaign;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface EmailRepository extends PagingAndSortingRepository<Email, Long> {

  List<Email> findByCampaign(Campaign campaign);

  @Deprecated
  // TODO: should not be used at all
  List<Email> findAll();

  List<Email> findByToContainsIgnoreCase(String emailFragment);

  List<Email> findByToContainingIgnoreCaseOrCampaignNameContainingIgnoreCaseOrCampaignEventNameContainingIgnoreCaseOrSubjectContainingOrBodyContaining(String to, String campaignName, String eventName, String subject, String body);
}
