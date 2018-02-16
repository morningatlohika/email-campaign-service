package com.lohika.morning.ecs.domain.attendee;

import com.github.javafaker.Faker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Service
public class AttendeeService {
  @Autowired
  private AttendeeRepository attendeeRepository;

  private Faker faker = new Faker();

  void reLoad() {
    attendeeRepository.deleteAll();
    List<Attendee> collect = IntStream.range(1, 100).mapToObj(i -> {

      String firstName = faker.name().firstName();
      String secondName = faker.name().lastName();
      String email = String.valueOf(firstName.charAt(0)).toLowerCase() + secondName.toLowerCase()
                     + "@" + faker.pokemon().name().toLowerCase() + ".com";

      return Attendee.builder()
          .firstName(firstName)
          .secondName(secondName)
          .email(email)
          .build();
    }).collect(toList());
    attendeeRepository.save(collect);
  }

  public List<Attendee> findAll() {
    return attendeeRepository.findAll();
  }

  public List<Attendee> filterBy(String value) {
    List<String> strings = stream(value.split("\\s+"))
        .filter(text -> !StringUtils.isEmpty(text))
        .collect(toList());

    return attendeeRepository.filterBy(strings);
  }
}
