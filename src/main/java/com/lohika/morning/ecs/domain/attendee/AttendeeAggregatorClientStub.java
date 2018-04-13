package com.lohika.morning.ecs.domain.attendee;

import com.github.javafaker.Faker;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Service
@ConditionalOnProperty(prefix = "attendee-aggregator", name = "enabled", havingValue = "false", matchIfMissing = true)
public class AttendeeAggregatorClientStub implements AttendeeAggregatorClient {

  private final Faker faker = new Faker();

  @Override
  public List<Attendee> load() {
    return IntStream.range(1, 100).mapToObj(i -> {

      String firstName = faker.name().firstName();
      String lastName = faker.name().lastName();
      String email = String.valueOf(firstName.charAt(0)).toLowerCase() + lastName.toLowerCase()
                     + "@" + faker.pokemon().name().toLowerCase() + ".com";

      return Attendee.builder()
          .firstName(firstName)
          .lastName(lastName)
          .email(email)
          .build();
    }).collect(toList());
  }
}
