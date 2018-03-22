package com.lohika.morning.ecs.domain.campaign;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.lohika.morning.ecs.domain.event.EventDataProvider;
import com.lohika.morning.ecs.domain.event.EventService;
import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import static java.lang.Math.abs;
import static java.util.Arrays.asList;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = CampaignEditView.VIEW_NAME)
public class CampaignEditView extends HorizontalLayout implements View {
  public static final String VIEW_NAME = "addCampaign";
  private static final List<Integer> PERIOD_ITEMS = asList(-14, -7 - 5, 0, 1, 7, 14);

  private final TextField name = new TextField("Name");
  private final TextField subject = new TextField("Subject");
  private final RichTextArea body = new RichTextArea("Body");
  private final ComboBox<Integer> priority = new ComboBox<>("Select priority", PERIOD_ITEMS);
  private final CheckBox attendee = new CheckBox("For all attendee");
  private final TextField emails = new TextField("Emails");

  private final Label eventName = new Label("Event name");
  private final ComboBox<MorningEvent> eventComboBox = new ComboBox<>("Event");
  private final EventDataProvider eventDataProvider;
  private final Label parentTemplateName = new Label("Parent template name");

  private final Button saveButton = new Button("Save", VaadinIcons.CHECK);
  private final Button deleteButton = new Button("Delete", VaadinIcons.TRASH);
  private final Button cancelButton = new Button("Cancel");
  private final Binder<Campaign> binder = new BeanValidationBinder<>(Campaign.class);

  private final CampaignService campaignService;
  private final EventService eventService;

  @PostConstruct
  public void init() {

    HorizontalLayout actions = new HorizontalLayout(saveButton, deleteButton, cancelButton);

    FormLayout form = new FormLayout(name, subject, body, attendee, emails, priority, actions);

    VerticalLayout details = new VerticalLayout(eventComboBox, eventName, parentTemplateName);
    addComponents(form, details);

    binder.bindInstanceFields(this);

    attendee.addValueChangeListener(this::onAttendeeChange);

    priority.setItemCaptionGenerator(this::generatePriorityCaption);
    priority.setEmptySelectionAllowed(false);

    saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
    saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    saveButton.addClickListener(this::editCampaign);

    deleteButton.addClickListener(this::deleteCampaign);

    cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    cancelButton.addClickListener(this::cancelCampaign);
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

  private void editCampaign(Button.ClickEvent clickEvent) {
    if (binder.validate().hasErrors()) {
      return;
    }
    Campaign campaign = binder.getBean();
    if (eventComboBox.isVisible()) {
      campaign.setEvent(eventComboBox.getValue());
    }
    campaignService.save(campaign);
    getUI().getNavigator().navigateTo(CampaignListView.VIEW_NAME);
  }

  private void deleteCampaign(Button.ClickEvent clickEvent) {
    Campaign campaign = binder.getBean();
    campaignService.delete(campaign);
    getUI().getNavigator().navigateTo(CampaignListView.VIEW_NAME);
  }

  private void cancelCampaign(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(CampaignListView.VIEW_NAME);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    Campaign campaign = getCampaign(viewChangeEvent);
    binder.setBean(campaign);

    eventName.setVisible(campaign.getEvent() != null);
    eventComboBox.setVisible(!eventName.isVisible());
    if (eventName.isVisible()) {
      eventName.setCaption(campaign.getEvent().getName());
    } else {
      eventComboBox.setDataProvider(eventDataProvider);
      eventComboBox.setItemCaptionGenerator(MorningEvent::getName);
    }

    parentTemplateName.setVisible(campaign.getCampaignTemplate() != null);
    if (parentTemplateName.isVisible()) {
      parentTemplateName.setCaption(campaign.getCampaignTemplate().getName());
    }

    deleteButton.setEnabled(isEntityPersisted());
  }

  private Campaign getCampaign(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    if (StringUtils.EMPTY.equals(viewChangeEvent.getParameters())) {
      return campaignService.newCampaign();
    }
    Long id = Long.valueOf(viewChangeEvent.getParameters());
    return campaignService.findOne(id);
  }
}
