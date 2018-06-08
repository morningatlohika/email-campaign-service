package com.lohika.morning.ecs.menu;

import com.lohika.morning.ecs.domain.attendee.AttendeeListView;
import com.lohika.morning.ecs.domain.campaign.CampaignListView;
import com.lohika.morning.ecs.domain.event.EventListView;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

public class TopMenu extends BaseMenu {

  private final Button manager;
  private final Button configurator;
  private final Button administrator;

  public TopMenu(UI ui) {
    super(ui);

    manager = createNavigationButton("Event manager", EventListView.VIEW_NAME);
    configurator = createNavigationButton("Configurator", AttendeeListView.VIEW_NAME);
    administrator = createNavigationButton("Administrator", CampaignListView.VIEW_NAME);

    addComponent(manager);
    addComponent(configurator);
    addComponent(administrator);
  }

  public void setActive(boolean administratorMenuVisible, boolean configurationMenuVisible, boolean managerMenuVisible) {
    if (administratorMenuVisible) {
      administrator.addStyleName(ValoTheme.BUTTON_PRIMARY);
    } else if (configurationMenuVisible) {
      configurator.addStyleName(ValoTheme.BUTTON_PRIMARY);
    } else if (managerMenuVisible) {
      manager.addStyleName(ValoTheme.BUTTON_PRIMARY);
    }
  }
}
