package com.lohika.morning.ecs.domain.talk;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class TalkPanel extends HorizontalLayout {

  private Button editBtn = new Button("Edit", VaadinIcons.EDIT);
  private HorizontalLayout actions = new HorizontalLayout(editBtn);
  private Label theses;

  public TalkPanel(Talk talk, boolean isEditable) {
    theses = new Label(talk.getTitle());
    addComponents(theses, actions);

    editBtn.setEnabled(isEditable);

    editBtn.addClickListener(clickEvent -> getUI().getNavigator().navigateTo(TalkEditorView.VIEW_NAME
                                                                             + "/"
                                                                             + talk.getId()));
  }
}
