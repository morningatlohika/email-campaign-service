package com.lohika.morning.ecs.domain.unsubscribe;

import lombok.extern.slf4j.Slf4j;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.themes.ValoTheme;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringView(name = UnsubscribeAddView.VIEW_NAME)
@Slf4j
public class UnsubscribeAddView extends HorizontalLayout implements View {
  public static final String VIEW_NAME = "addUnsubscribe";

  private TextArea emails = new TextArea("Comma separated emails");

  private Button saveButton = new Button("Save", VaadinIcons.CHECK);
  private Button cancelButton = new Button("Cancel");

  @Autowired
  private UnsubscribeService unsubscribeService;

  @PostConstruct
  public void init() {
    addComponents(new FormLayout(emails, new HorizontalLayout(saveButton, cancelButton)));

    saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
    saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    saveButton.addClickListener(clickEvent -> {
      unsubscribeService.add(emails.getValue());
      getUI().getNavigator().navigateTo(UnsubscribeListView.VIEW_NAME);
    });

    cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    cancelButton.addClickListener(clickEvent -> getUI().getNavigator().navigateTo(UnsubscribeListView.VIEW_NAME));
  }
}
