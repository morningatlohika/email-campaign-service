package com.lohika.morning.ecs.ui;

import com.lohika.morning.ecs.domain.event.EventService;
import com.lohika.morning.ecs.menu.MainMenu;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
@SpringUI(path = "/")
@SpringViewDisplay
public class MainLayout extends UI implements ViewDisplay {
  private final MainMenu mainMenu;
  private final Panel springViewDisplay = new Panel();

  public MainLayout(EventService eventService) {
    mainMenu = new MainMenu(getUI(), eventService.findAll());
  }

  @Override
  protected void init(VaadinRequest request) {
    // Configure the errorMessage handler for the UI
    UI.getCurrent().setErrorHandler(new ExceptionHandler());

    final VerticalLayout root = new VerticalLayout();
    root.setSizeFull();

    root.addComponents(mainMenu, springViewDisplay);

    springViewDisplay.setSizeFull();
    root.setExpandRatio(springViewDisplay, 1.0f);

    setContent(root);
  }

  @Override
  public void showView(View view) {
    mainMenu.showView();
    springViewDisplay.setContent((Component) view);
  }
}
