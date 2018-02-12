package com.lohika.morning.ecs.domain.talk;

import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TabSheet;

import java.util.List;

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

        talks.forEach(talk -> addTab(new TalkPanel(talk), talk.getTitle()));
    }

}
