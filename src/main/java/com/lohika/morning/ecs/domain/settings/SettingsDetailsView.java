package com.lohika.morning.ecs.domain.settings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.lohika.morning.ecs.domain.event.EventListView;
import com.lohika.morning.ecs.vaadin.EcsLabel;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = SettingsDetailsView.VIEW_NAME)
public class SettingsDetailsView extends HorizontalLayout implements View {
  public static final String VIEW_NAME = "settingsDetails";

  private final EcsLabel signature = new EcsLabel("Signature");

  private final Binder<Settings> binder = new BeanValidationBinder<>(Settings.class);

  private final Button editButton = new Button("Edit", VaadinIcons.EDIT);
  private final Button closeButton = new Button("Close");

  private final SettingsService settingsService;

  @PostConstruct
  public void init() {
    HorizontalLayout actions = new HorizontalLayout(editButton, closeButton);
    FormLayout form = new FormLayout(signature, actions);
    addComponents(form);

    signature.setContentMode(ContentMode.HTML);

    editButton.addClickListener(this::editSettings);
    closeButton.addClickListener(this::cancelSettings);
  }

  private void editSettings(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(SettingsEditView.VIEW_NAME);
  }

  private void cancelSettings(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(EventListView.VIEW_NAME);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    Settings settings = settingsService.getSettings();
    binder.setBean(settings);
    binder.bindInstanceFields(this);
  }
}