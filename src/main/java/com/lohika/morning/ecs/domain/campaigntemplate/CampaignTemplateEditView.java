package com.lohika.morning.ecs.domain.campaigntemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.lohika.morning.ecs.utils.PriorityUtil;
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

@Slf4j
@RequiredArgsConstructor
@SpringView(name = CampaignTemplateEditView.VIEW_NAME)
public class CampaignTemplateEditView extends HorizontalLayout implements View {
  public static final String VIEW_NAME = "addCampaignTemplate";
  private static final List<Integer> PERIOD_ITEMS = PriorityUtil.getPriorities();
  private final TextField name = new TextField("Name");
  private final TextField subject = new TextField("Subject");
  private final RichTextArea body = new RichTextArea("Body");
  private final ComboBox<Integer> priority = new ComboBox<>("Select priority", PERIOD_ITEMS);
  private final CheckBox attendee = new CheckBox("For all attendee");
  private final TextField emails = new TextField("Emails");

  private final CheckBox ready = new CheckBox("Template ready for use");

  private final Button saveButton = new Button("Save", VaadinIcons.CHECK);
  private final Button deleteButton = new Button("Delete", VaadinIcons.TRASH);
  private final Button cancelButton = new Button("Cancel");
  private final Binder<CampaignTemplate> binder = new BeanValidationBinder<>(CampaignTemplate.class);

  private final CampaignTemplateService campaignTemplateService;

  @PostConstruct
  public void init() {

    HorizontalLayout actions = new HorizontalLayout(saveButton, deleteButton, cancelButton);

    FormLayout form = new FormLayout(name, subject, body, attendee, emails, priority, ready, actions);
    addComponents(form);

    binder.bindInstanceFields(this);

    attendee.addValueChangeListener(this::onAttendeeChange);

    priority.setItemCaptionGenerator(this::generatePriorityCaption);
    priority.setEmptySelectionAllowed(false);

    saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
    saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    saveButton.addClickListener(this::editCampaignTemplate);

    deleteButton.addClickListener(this::deleteCampaignTemplate);

    cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    cancelButton.addClickListener(this::cancelCampaignTemplate);
  }

  private boolean isEntityPersisted() {
    return binder.getBean().getId() != null;
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

  private void editCampaignTemplate(Button.ClickEvent clickEvent) {
    if (binder.validate().hasErrors()) {
      return;
    }
    CampaignTemplate template = binder.getBean();
    campaignTemplateService.save(template);
    getUI().getNavigator().navigateTo(CampaignTemplateListView.VIEW_NAME);
  }

  private void deleteCampaignTemplate(Button.ClickEvent clickEvent) {
    CampaignTemplate template = binder.getBean();
    campaignTemplateService.delete(template);
    getUI().getNavigator().navigateTo(CampaignTemplateListView.VIEW_NAME);
  }

  private void cancelCampaignTemplate(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(CampaignTemplateListView.VIEW_NAME);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    CampaignTemplate template = getCampaignTemplate(viewChangeEvent);
    binder.setBean(template);
    deleteButton.setEnabled(isEntityPersisted());
  }

  private CampaignTemplate getCampaignTemplate(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    if (StringUtils.EMPTY.equals(viewChangeEvent.getParameters())) {
      return campaignTemplateService.newTemplate();
    }
    Long id = Long.valueOf(viewChangeEvent.getParameters());
    return campaignTemplateService.findOne(id);
  }
}
