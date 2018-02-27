package com.lohika.morning.ecs.exception;

import org.slf4j.helpers.MessageFormatter;

public class ResourceNotFoundException extends RuntimeException {
  public static final String MESSAGE_PATTERN_SINGULAR = "Resource {}#{} does not exist.";
  public static final String MESSAGE_PATTERN_PLURAL = "Resources {}#{} do not exist.";
  public static final String MESSAGE_PATTERN_EMBEDDED = "Resource {}#{}/{}#{} does not exist.";

  public <T> ResourceNotFoundException(Class<T> entityClass, long... ids) {
    super(MessageFormatter.format(
            ids.length > 1 ? MESSAGE_PATTERN_PLURAL : MESSAGE_PATTERN_SINGULAR,
            entityClass.getSimpleName(),
            ids.length == 1 ? ids[0] : ids).getMessage());
  }

  public <T> ResourceNotFoundException(Class<T> entityClass, String name) {
    super(MessageFormatter.format(
            MESSAGE_PATTERN_SINGULAR,
            entityClass.getSimpleName(),
            name).getMessage());
  }

  public <O, T> ResourceNotFoundException(Class<O> owningEntityClass, long owningEntityId, Class<T> missingEntityClass, long missingEntityId) {
    super(MessageFormatter.arrayFormat(MESSAGE_PATTERN_EMBEDDED,
            new Object[]{
                    owningEntityClass.getSimpleName(),
                    owningEntityId,
                    missingEntityClass.getSimpleName(),
                    missingEntityId}
    ).getMessage());
  }

  public ResourceNotFoundException(Throwable cause) {
    super(cause.getMessage(), cause);
  }
}