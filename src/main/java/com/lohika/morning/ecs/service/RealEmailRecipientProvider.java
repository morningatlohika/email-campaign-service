package com.lohika.morning.ecs.service;

import com.lohika.morning.ecs.domain.email.Email;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 This bean should be wired IF AND ONLY IF the profile is prod AND
 the <code>email-client-service.recipient-email-override</code>flag is set to <code>false</code> explicitly!
 <p>
 This way we ca guarantee that in the non-prod profile no one can accidentally turn on sending emails to real recipients.
*/
@Service
@Profile("prod")
@ConditionalOnProperty(name = "email-client-service.recipient-email-override", havingValue = "false")
public class RealEmailRecipientProvider implements EmailRecipientProvider {
  @Override
  public String getRecipientEmail(Email email) {
    return email.getTo();
  }

  @Override
  public String getCcEmail(Email email) {
    return email.getCc();
  }

  @Override
  public String getBccEmail(Email email) {
    return email.getBcc();
  }
}