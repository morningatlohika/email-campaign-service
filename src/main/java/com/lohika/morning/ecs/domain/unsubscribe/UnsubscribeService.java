package com.lohika.morning.ecs.domain.unsubscribe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Service
public class UnsubscribeService {
  @Autowired
  private UnsubscribeRepository unsubscribeRepository;

  public List<Unsubscribe> findAll() {
    return unsubscribeRepository.findAll();
  }

  public List<Unsubscribe> filterByEmail(String email) {
    return unsubscribeRepository.filterAllByEmail(email);
  }

  public void add(String string) {
    List<Unsubscribe> emails = stream(string.split("\\s+"))
        .map(email -> email.replace(",", ""))
        .filter(email -> !StringUtils.isEmpty(email))
        .map(email -> Unsubscribe.builder().email(email).build())
        .collect(toList());

    emails.stream()
        .filter(unsubscribe -> CollectionUtils.isEmpty(unsubscribeRepository.findAllByEmail(unsubscribe.getEmail())))
        .forEach(email -> unsubscribeRepository.save(email));
  }

  public void delete(Unsubscribe unsubscribe) {
    unsubscribeRepository.delete(unsubscribe);
  }
}
