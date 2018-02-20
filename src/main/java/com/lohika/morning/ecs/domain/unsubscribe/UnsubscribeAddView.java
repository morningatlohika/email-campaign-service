package com.lohika.morning.ecs.domain.unsubscribe;

import lombok.RequiredArgsConstructor;
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

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = UnsubscribeAddView.VIEW_NAME)
public class UnsubscribeAddView extends HorizontalLayout implements View {
  public static final String VIEW_NAME = "addUnsubscribe";
  private final UnsubscribeService unsubscribeService;
  private TextArea emails = new TextArea("Comma separated emails");
  private Button saveButton = new Button("Save", VaadinIcons.CHECK);
  private Button cancelButton = new Button("Cancel");

  @PostConstruct
  public void init() {
    addComponents(new FormLayout(emails, new HorizontalLayout(saveButton, cancelButton)));

    saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
    saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    saveButton.addClickListener(this::createUnsubscribe);

    cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    cancelButton.addClickListener(clickEvent -> getUI().getNavigator().navigateTo(UnsubscribeListView.VIEW_NAME));
  }

  private void createUnsubscribe(Button.ClickEvent clickEvent) {
    unsubscribeService.add(emails.getValue());
    getUI().getNavigator().navigateTo(UnsubscribeListView.VIEW_NAME);
  }
}
