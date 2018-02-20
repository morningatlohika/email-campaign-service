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
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import javax.annotation.PostConstruct;

import static org.apache.commons.lang3.tuple.Pair.of;

import static java.util.Arrays.asList;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = TemplateEditView.VIEW_NAME)
public class TemplateEditView extends HorizontalLayout implements View {
  public static final String VIEW_NAME = "addTemplate";
  public static final List<Pair<Integer, String>> PERIOD_ITEMS
      = asList(of(1, "a"), of(2, "b"));

  private final TextField name = new TextField("Name");
  private final TextField subject = new TextField("Subject");
  private final RichTextArea body = new RichTextArea("Body");
  private final ComboBox<Pair<Integer, String>> priorityBox = new ComboBox<>("Select priority", PERIOD_ITEMS);
  private final CheckBox attendee = new CheckBox("For all attendee");
  private final TextField emails = new TextField("Emails");


  private final Button saveButton = new Button("Save", VaadinIcons.CHECK);
  private final Button cancelButton = new Button("Cancel");
  private final Binder<Template> validation = new BeanValidationBinder<>(Template.class);
  private final TemplateService templateService;
  private Button deleteButton = new Button("Delete", VaadinIcons.TRASH);

  @PostConstruct
  public void init() {

    HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton, cancelButton);

    FormLayout form = new FormLayout(name, subject, body, attendee, emails, priorityBox, buttons);
    addComponents(form);

    validation.bindInstanceFields(this);

    attendee.addValueChangeListener(this::onAttendeeChange);

    priorityBox.setItemCaptionGenerator(Pair::getValue);

    saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
    saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    saveButton.addClickListener(this::createTemplate);

    cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    cancelButton.addClickListener(this::cancelTemplate);
  }

  private void onAttendeeChange(HasValue.ValueChangeEvent<Boolean> e) {
    emails.setVisible(!e.getValue());
  }

  private void createTemplate(Button.ClickEvent clickEvent) {
    Template template = Template.builder()
        .name(name.getValue())
        .subject(subject.getValue())
        .body(body.getValue())
        .priority(priorityBox.getValue().getKey())
        .attendee(attendee.getValue())
        .emails(emails.getValue())
        .build();

    templateService.save(template);
    getUI().getNavigator().navigateTo(TemplateListView.VIEW_NAME);
  }

  private void cancelTemplate(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(TemplateListView.VIEW_NAME);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    Template template = getTemplate(viewChangeEvent);
    validation.setBean(template);
  }

  private Template getTemplate(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    if (StringUtils.EMPTY.equals(viewChangeEvent.getParameters())) {
      return templateService.newTemplate();
    }
    Long id = Long.valueOf(viewChangeEvent.getParameters());
    return templateService.findOne(id);
  }
}
