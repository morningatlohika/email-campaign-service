package com.lohika.morning.ecs.domain.unsubscribe;

import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.themes.ValoTheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = UnsubscribeAddView.VIEW_NAME)
public class UnsubscribeAddView extends HorizontalLayout implements View {
  public static final String VIEW_NAME = "addUnsubscribe";
  private final UnsubscribeService unsubscribeService;
  private TextArea emails = new TextArea("Use comma, space, semicolon or newline to separated emails");
  private Button saveButton = new Button("Save", VaadinIcons.CHECK);
  private Button cancelButton = new Button("Cancel");

  @PostConstruct
  public void init() {
    addComponents(new FormLayout(emails, new HorizontalLayout(saveButton, cancelButton)));

    saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
    saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    saveButton.addClickListener(this::createUnsubscribes);

    cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    cancelButton.addClickListener(this::backToList);
  }

  private void backToList(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(UnsubscribeListView.VIEW_NAME);
  }

  private void createUnsubscribes(Button.ClickEvent clickEvent) {
    unsubscribeService.add(emails.getValue());
    backToList(clickEvent);
  }
}
