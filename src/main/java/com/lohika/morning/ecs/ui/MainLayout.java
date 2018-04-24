package com.lohika.morning.ecs.ui;

import com.lohika.morning.ecs.domain.attendee.AttendeeListView;
import com.lohika.morning.ecs.domain.campaign.CampaignListView;
import com.lohika.morning.ecs.domain.campaigntemplate.CampaignTemplateListView;
import com.lohika.morning.ecs.domain.email.EmailListView;
import com.lohika.morning.ecs.domain.event.EventListView;
import com.lohika.morning.ecs.domain.settings.SettingsDetailsView;
import com.lohika.morning.ecs.domain.unsubscribe.UnsubscribeListView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("valo")
@SpringUI(path = "/")
@SpringViewDisplay
public class MainLayout extends UI implements ViewDisplay {
  private Panel springViewDisplay;

  @Override
  protected void init(VaadinRequest request) {
    final VerticalLayout root = new VerticalLayout();
    root.setSizeFull();
    setContent(root);

    final CssLayout navigationBar = new CssLayout();
    navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
    navigationBar.addComponent(createNavigationButton("Events", EventListView.VIEW_NAME));
    navigationBar.addComponent(createNavigationButton("Campaigns", CampaignListView.VIEW_NAME));
    navigationBar.addComponent(createNavigationButton("Emails", EmailListView.VIEW_NAME));
    navigationBar.addComponent(createNavigationButton("Attendees", AttendeeListView.VIEW_NAME));
    navigationBar.addComponent(createNavigationButton("Unsubscribe", UnsubscribeListView.VIEW_NAME));
    navigationBar.addComponent(createNavigationButton("Campaign template", CampaignTemplateListView.VIEW_NAME));
    navigationBar.addComponent(createNavigationButton("Settings", SettingsDetailsView.VIEW_NAME));
    root.addComponent(navigationBar);

    VerticalLayout popupContent = new VerticalLayout();
    PopupView popup = new PopupView(null, popupContent);
    Label errorMessage = new Label();
    popupContent.addComponent(errorMessage);
    root.addComponent(popup);

    springViewDisplay = new Panel();
    springViewDisplay.setSizeFull();
    root.addComponent(springViewDisplay);
    root.setExpandRatio(springViewDisplay, 1.0f);

    // Configure the errorMessage handler for the UI
    UI.getCurrent().setErrorHandler(new DefaultErrorHandler() {
      @Override
      public void error(com.vaadin.server.ErrorEvent event) {
        // Find the final cause
        Throwable cause = event.getThrowable();
        while (cause.getCause() != null) {
          cause = cause.getCause();
        }

        errorMessage.setValue(cause.getMessage());
        popup.setPopupVisible(true);

        // Do the default errorMessage handling (optional)
        doDefault(event);
      }
    });
  }

  private Button createNavigationButton(String caption, final String viewName) {
    Button button = new Button(caption);
    button.addStyleName(ValoTheme.BUTTON_SMALL);
    // If you didn't choose Java 8 when creating the project, convert this
    // to an anonymous listener class
    button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
    return button;
  }

  @Override
  public void showView(View view) {
    springViewDisplay.setContent((Component) view);
  }
}
