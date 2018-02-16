package com.lohika.morning.ecs.domain.attendee;


import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class AttendeeRepositoryImpl implements AttendeeRepositoryCustom {
  @PersistenceContext
  private EntityManager entityManager;

  public List<Attendee> filterBy(List<String> strings) {
    String sql = "SELECT * FROM Attendees  WHERE "
                 + strings.stream()
                     .map(text -> " first_name like '%" + text + "%'"
                                  + " or second_name like '%" + text + "%'"
                                  + " or email like '%" + text + "%'")
                     .collect(Collectors.joining(" or"));

    Query query = entityManager.createNativeQuery(sql, Attendee.class);
    return query.getResultList();
  }
}
