package com.lohika.morning.ecs.menu;

import com.lohika.morning.ecs.domain.applicationstatus.ApplicationStateDetailsView;
import com.lohika.morning.ecs.domain.campaign.CampaignListView;
import com.lohika.morning.ecs.domain.email.EmailListView;
import com.lohika.morning.ecs.domain.talk.TalkListView;
import com.vaadin.ui.UI;

public class AdministratorMenu extends BaseMenu {

  public AdministratorMenu(UI ui) {
    super(ui);
//    addComponent(createNavigationButton("Events", EventListView.VIEW_NAME));
    addComponent(createNavigationButton("Campaigns", CampaignListView.VIEW_NAME));
    addComponent(createNavigationButton("Talks", TalkListView.VIEW_NAME));
    addComponent(createNavigationButton("Emails", EmailListView.VIEW_NAME));
    addComponent(createNavigationButton("State", ApplicationStateDetailsView.VIEW_NAME));
  }
}
