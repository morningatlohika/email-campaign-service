package com.lohika.morning.ecs.domain.applicationstatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.lohika.morning.ecs.domain.attendee.AttendeeService;
import com.lohika.morning.ecs.domain.campaign.CampaignService;
import com.lohika.morning.ecs.domain.campaigntemplate.CampaignTemplate;
import com.lohika.morning.ecs.domain.campaigntemplate.CampaignTemplateService;
import com.lohika.morning.ecs.domain.email.EmailService;
import com.lohika.morning.ecs.domain.event.EventListView;
import com.lohika.morning.ecs.domain.settings.Settings;
import com.lohika.morning.ecs.domain.settings.SettingsEditView;
import com.lohika.morning.ecs.domain.settings.SettingsService;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import java.util.List;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = ApplicationStateDetailsView.VIEW_NAME)
public class ApplicationStateDetailsView extends VerticalLayout implements View {
  public static final String VIEW_NAME = "applicationState";

  private final EcsLabel attendeeCountLabel = new EcsLabel("Count of attendee");
  private final EcsLabel lastUpdateAttendeeLabel = new EcsLabel("Time of last update");
  private final HorizontalLayout attendees = new HorizontalLayout(attendeeCountLabel, lastUpdateAttendeeLabel);

  private final EcsLabel campaignCountLabel = new EcsLabel("Count of campaign");
  private final EcsLabel campaignTemplateCountLabel = new EcsLabel("Count of campaign template");
  private final EcsLabel activeCampaignTemplateCountLabel = new EcsLabel("Count of active campaign template");
  private final EcsLabel lastProcessCampaignLabel = new EcsLabel("Time process campaign");
  private final HorizontalLayout campaign = new HorizontalLayout(campaignCountLabel, campaignTemplateCountLabel,
      activeCampaignTemplateCountLabel, lastProcessCampaignLabel);

  private final EcsLabel emailCountLabel = new EcsLabel("Count of emails");
  private final EcsLabel lastSendEmailUpdateLabel = new EcsLabel("Time send email");
  private final HorizontalLayout emails = new HorizontalLayout(emailCountLabel, lastSendEmailUpdateLabel);

  private final ApplicationStateService applicationStateService;
  private final AttendeeService attendeeService;
  private final CampaignService campaignService;
  private final CampaignTemplateService campaignTemplateService;
  private final EmailService emailService;

  @PostConstruct
  public void init() {
    addComponents(attendees, campaign, emails);

    attendeeCountLabel.setValue(String.valueOf(attendeeService.count()));
    lastUpdateAttendeeLabel.setValue(applicationStateService.getUpdateAttendee());

    campaignCountLabel.setValue(String.valueOf(campaignService.count()));
    campaignTemplateCountLabel.setValue(String.valueOf(campaignTemplateService.count()));
    activeCampaignTemplateCountLabel.setValue(String.valueOf(campaignTemplateService.countReady()));

    lastProcessCampaignLabel.setValue(applicationStateService.getProcessCampaign());

    emailCountLabel.setValue(String.valueOf(emailService.count()));
    lastSendEmailUpdateLabel.setValue(applicationStateService.getSendEmail());
  }
}
