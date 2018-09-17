package com.lohika.morning.ecs.service;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class HelpService {

  public static String VARIABLE;

  @PostConstruct
  public void init() throws IOException {
    VARIABLE = load("help/variable.html");
  }

  private String load(String filename) throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    return IOUtils.toString(Objects.requireNonNull(classLoader.getResource(filename)));
  }
}
