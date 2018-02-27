package com.lohika.morning.ecs.domain.talk;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@SpringView(name = TalkView.VIEW_NAME)
@Slf4j
public class TalkView extends VerticalLayout implements View {

  public static final String VIEW_NAME = "talks";

  @PostConstruct
  void init() {
    addComponent(new Label("This is a talks view"));
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {
    log.debug("Opening talks view");
  }
}
