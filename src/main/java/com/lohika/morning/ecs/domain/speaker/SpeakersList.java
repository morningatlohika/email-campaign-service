package com.lohika.morning.ecs.domain.speaker;

import com.lohika.morning.ecs.domain.talk.Talk;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TabSheet;

@SpringComponent
@UIScope
public class SpeakersList extends TabSheet {

  public SpeakersList() {
    setWidth(500, Unit.PIXELS);
    setHeight(500, Unit.PIXELS);
  }

  public void displaySpeakers(Talk talk) {
    removeAllComponents();
    talk.getSpeakers().forEach(speaker -> addTab(new SpeakerPanel(speaker), speaker.getFullName()));
  }

}
