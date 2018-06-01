package com.lohika.morning.ecs.service;

import com.lohika.morning.ecs.domain.email.Email;

public interface EmailRecipientProvider {
  String getRecipientEmail(Email email);
}
