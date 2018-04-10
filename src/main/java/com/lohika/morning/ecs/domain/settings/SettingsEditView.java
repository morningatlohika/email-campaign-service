package com.lohika.morning.ecs.domain.settings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.themes.ValoTheme;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = SettingsEditView.VIEW_NAME)
public class SettingsEditView extends HorizontalLayout implements View {
  public static final String VIEW_NAME = "settingsEdit";

  private final RichTextArea signature = new RichTextArea("Signature");

  private final Button saveButton = new Button("Save", VaadinIcons.CHECK);
  private final Button cancelButton = new Button("Cancel");
  private final Binder<Settings> binder = new BeanValidationBinder<>(Settings.class);

  private final SettingsService settingsService;

  @PostConstruct
  public void init() {

    HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton);

    FormLayout form = new FormLayout(signature, actions);

    addComponents(form);

    binder.bindInstanceFields(this);

    saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
    saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    saveButton.addClickListener(this::editSettings);

    cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    cancelButton.addClickListener(this::cancelSettings);
  }

  private void editSettings(Button.ClickEvent clickEvent) {
    if (binder.validate().hasErrors()) {
      return;
    }
    Settings settings = binder.getBean();
    settingsService.save(settings);
    getUI().getNavigator().navigateTo(SettingsDetailsView.VIEW_NAME);
  }

  private void cancelSettings(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(SettingsDetailsView.VIEW_NAME);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    Settings settings = settingsService.getSettings();
    binder.setBean(settings);
  }
}
