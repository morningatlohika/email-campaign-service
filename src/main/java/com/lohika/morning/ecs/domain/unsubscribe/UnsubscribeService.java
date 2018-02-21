package com.lohika.morning.ecs.domain.unsubscribe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
public class UnsubscribeService {
  @Autowired
  private UnsubscribeRepository unsubscribeRepository;

  public List<Unsubscribe> findAll() {
    return unsubscribeRepository.findAll();
  }

  public List<Unsubscribe> filterBy(String email) {
    return unsubscribeRepository.findByEmailContaining(email);
  }

  public void add(String emails) {
    Set<String> setOfEmails = stream(emails.split(","))
        .map(String::trim)
        .filter(email -> !StringUtils.isEmpty(email))
        .collect(toSet());

    Set<String> existingEmails = unsubscribeRepository.findByEmailIn(setOfEmails).stream()
        .map(Unsubscribe::getEmail)
        .collect(toSet());

    Set<Unsubscribe> newEmails = setOfEmails.stream()
        .filter(email -> !existingEmails.contains(email))
        .map(email -> Unsubscribe.builder().email(email).build())
        .collect(toSet());

    unsubscribeRepository.save(newEmails);
  }

  public void delete(Unsubscribe unsubscribe) {
    unsubscribeRepository.delete(unsubscribe);
  }
}
