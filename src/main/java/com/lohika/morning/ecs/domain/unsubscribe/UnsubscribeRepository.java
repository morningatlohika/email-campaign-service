package com.lohika.morning.ecs.domain.unsubscribe;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnsubscribeRepository extends PagingAndSortingRepository<Unsubscribe, Long> {
  List<Unsubscribe> findAll();

  List<Unsubscribe> findAllByEmail(String email);

  @Query("SELECT u FROM Unsubscribe u WHERE u.email like %:string%")
  List<Unsubscribe> filterAllByEmail(@Param("string") String string);
}
