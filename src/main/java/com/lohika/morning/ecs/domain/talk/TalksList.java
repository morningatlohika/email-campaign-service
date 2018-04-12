package com.lohika.morning.ecs.domain.talk;

import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;

import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.lohika.morning.ecs.utils.EcsUtils.isEditable;

@SpringComponent
@UIScope
public class TalksList extends TabSheet {
  private final TalkService talkService;

  public TalksList(TalkService talkService) {
    this.talkService = talkService;
    setWidth(100, Unit.PERCENTAGE);
    setHeight(100, Unit.PERCENTAGE);
  }

  public void displayTalks(MorningEvent morningEvent) {
    removeAllComponents();
    List<Talk> talks = talkService.getTalks(morningEvent);

    if (CollectionUtils.isEmpty(talks)) {
      addComponent(new Label("There are no talks for this event yet"));
    }

    talks.forEach(talk -> addTab(new TalkPanel(talk, isEditable(morningEvent)), talk.getTitle()));
  }

}
