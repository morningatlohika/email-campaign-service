package com.lohika.morning.ecs.domain.talk;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class TalkPanel extends VerticalLayout {

    private Button editBtn = new Button(VaadinIcons.EDIT);
    private HorizontalLayout actions = new HorizontalLayout(editBtn);
    private Label theses;

    public TalkPanel(Talk talk) {
        theses = new Label(talk.getTheses());
        theses.setWidth(100, Unit.PERCENTAGE);
        addComponents(actions, theses);

        editBtn.addClickListener(clickEvent -> getUI().getNavigator().navigateTo(TalkEditorView.VIEW_NAME + "/" + talk.getId()));
    }
}
