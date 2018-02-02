package com.lohika.morning.ecs.domain.talk;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class TalkPanel extends VerticalLayout {

    private Label theses;

    public TalkPanel(Talk talk) {
        theses = new Label(talk.getTheses());
        theses.setWidth(100, Unit.PERCENTAGE);
        addComponents(theses);
    }
}
