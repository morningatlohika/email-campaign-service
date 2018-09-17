package com.lohika.morning.ecs.service;

import com.lohika.morning.ecs.domain.email.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!prod")
public class OverrideEmailRecipientProvider implements EmailRecipientProvider {

  @Value("${email-client-service.recipient-email-override}")
  private String recipientEmailOverride;

  @Override
  public String getRecipientEmail(Email email) {
    return recipientEmailOverride;
  }

  @Override
  public String getCcEmail(Email email) {
    return recipientEmailOverride;
  }

  @Override
  public String getBccEmail(Email email) {
    return recipientEmailOverride;
  }
}
