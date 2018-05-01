package com.lohika.morning.ecs.menu;

import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import java.util.List;
import java.util.stream.StreamSupport;

public class MainMenu extends Panel {

  private final TopMenu topMenu;
  private final BaseMenu administratorMenu;
  private final BaseMenu configurationMenu;
  private final BaseMenu managerMenu;

  public MainMenu(UI ui, List<MorningEvent> events) {

    VerticalLayout menu = new VerticalLayout();
    topMenu = new TopMenu(ui);
    administratorMenu = new AdministratorMenu(ui);
    configurationMenu = new ConfigurationMenu(ui);
    managerMenu = new ManagerMenu(ui, events);

    menu.addComponents(topMenu, administratorMenu, configurationMenu, managerMenu);
    this.setContent(menu);
  }

  public void showView() {
    administratorMenu.setVisible(isMenuActive(administratorMenu));
    configurationMenu.setVisible(isMenuActive(configurationMenu));
    managerMenu.setVisible(isMenuActive(managerMenu));

    topMenu.setActive(administratorMenu.isVisible(), configurationMenu.isVisible(), managerMenu.isVisible());
  }

  private boolean isMenuActive(BaseMenu managerMenu) {
    return StreamSupport.stream(managerMenu.spliterator(), false)
        .anyMatch(component -> getUI().getNavigator().getState().equals(component.getId()));
  }
}
