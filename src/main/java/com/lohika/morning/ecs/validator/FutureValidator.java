package com.lohika.morning.ecs.validator;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Future;

public class FutureValidator implements ConstraintValidator<Future, LocalDate> {

  @Override
  public void initialize(Future constraintAnnotation) {
  }

  @Override
  public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
    return value == null || value.isAfter(LocalDate.now());
  }
}