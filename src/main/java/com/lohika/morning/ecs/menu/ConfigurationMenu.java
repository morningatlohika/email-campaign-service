package com.lohika.morning.ecs.menu;

import com.lohika.morning.ecs.domain.attendee.AttendeeListView;
import com.lohika.morning.ecs.domain.campaigntemplate.CampaignTemplateListView;
import com.lohika.morning.ecs.domain.settings.SettingsDetailsView;
import com.lohika.morning.ecs.domain.unsubscribe.UnsubscribeListView;
import com.vaadin.ui.UI;

public class ConfigurationMenu extends BaseMenu {

  public ConfigurationMenu(UI ui) {
    super(ui);
    addComponent(createNavigationButton("Attendees", AttendeeListView.VIEW_NAME));
    addComponent(createNavigationButton("Unsubscribe", UnsubscribeListView.VIEW_NAME));
    addComponent(createNavigationButton("Campaign template", CampaignTemplateListView.VIEW_NAME));
    addComponent(createNavigationButton("Settings", SettingsDetailsView.VIEW_NAME));
  }
}
