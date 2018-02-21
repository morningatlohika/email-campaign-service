package com.lohika.morning.ecs.domain.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.annotation.PostConstruct;

import static java.lang.Math.abs;
import static java.util.Arrays.asList;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = TemplateEditView.VIEW_NAME)
public class TemplateEditView extends HorizontalLayout implements View {
  public static final String VIEW_NAME = "addTemplate";
  private static final List<Integer> PERIOD_ITEMS = asList(-14, -7 - 5, 0, 1, 7, 14);

  private final TextField name = new TextField("Name");
  private final TextField subject = new TextField("Subject");
  private final RichTextArea body = new RichTextArea("Body");
  private final ComboBox<Integer> priority = new ComboBox<>("Select priority", PERIOD_ITEMS);
  private final CheckBox attendee = new CheckBox("For all attendee");
  private final TextField emails = new TextField("Emails");

  private final Button saveButton = new Button("Save", VaadinIcons.CHECK);
  private final Button deleteButton = new Button("Delete", VaadinIcons.TRASH);
  private final Button cancelButton = new Button("Cancel");
  private final Binder<Template> validation = new BeanValidationBinder<>(Template.class);

  private final TemplateService templateService;

  @PostConstruct
  public void init() {

    HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton, cancelButton);

    FormLayout form = new FormLayout(name, subject, body, attendee, emails, priority, buttons);
    addComponents(form);

    validation.bindInstanceFields(this);

    attendee.addValueChangeListener(this::onAttendeeChange);

    priority.setItemCaptionGenerator(this::generatePriorityCaption);
    priority.setEmptySelectionAllowed(false);

    saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
    saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    saveButton.addClickListener(this::createTemplate);

    deleteButton.addClickListener(this::deleteTemplate);
    deleteButton.setEnabled(isDeleteEnabled());

    cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    cancelButton.addClickListener(this::cancelTemplate);
  }

  private boolean isDeleteEnabled() {
    return validation.getBean() != null && validation.getBean().getId() != null;
  }

  private String generatePriorityCaption(Integer i) {
    if (i == 0) {
      return "in event day";
    } else if (i % 7 == 0) {
      return abs(i / 7) + " week(s) " + ((i > 0) ? "after" : "before");
    }
    return abs(i) + " day(s) " + ((i > 0) ? "after" : "before");
  }

  private void onAttendeeChange(HasValue.ValueChangeEvent<Boolean> e) {
    emails.setVisible(!e.getValue());
  }

  private void createTemplate(Button.ClickEvent clickEvent) {
    Template template = validation.getBean();
    templateService.save(template);
    getUI().getNavigator().navigateTo(TemplateListView.VIEW_NAME);
  }

  private void deleteTemplate(Button.ClickEvent clickEvent) {
    Template template = validation.getBean();
    templateService.delete(template);
    getUI().getNavigator().navigateTo(TemplateListView.VIEW_NAME);
  }

  private void cancelTemplate(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(TemplateListView.VIEW_NAME);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    Template template = getTemplate(viewChangeEvent);
    validation.setBean(template);
    deleteButton.setEnabled(isDeleteEnabled());
  }

  private Template getTemplate(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    if (StringUtils.EMPTY.equals(viewChangeEvent.getParameters())) {
      return templateService.newTemplate();
    }
    Long id = Long.valueOf(viewChangeEvent.getParameters());
    return templateService.findOne(id);
  }
}
