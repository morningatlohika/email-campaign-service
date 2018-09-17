package com.lohika.morning.ecs.menu;

import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import java.util.stream.StreamSupport;

public class MainMenu extends Panel {

  private final TopMenu topMenu;
  private final BaseMenu administratorMenu;
  private final BaseMenu configurationMenu;

  public MainMenu(UI ui) {

    VerticalLayout menu = new VerticalLayout();
    topMenu = new TopMenu(ui);
    administratorMenu = new AdministratorMenu(ui);
    configurationMenu = new ConfigurationMenu(ui);

    menu.addComponents(topMenu, administratorMenu, configurationMenu);
    this.setContent(menu);
  }

  public void showView() {
    administratorMenu.setVisible(isMenuActive(administratorMenu));
    configurationMenu.setVisible(isMenuActive(configurationMenu));

    topMenu.setActive(administratorMenu.isVisible(), configurationMenu.isVisible());
  }

  private boolean isMenuActive(BaseMenu managerMenu) {
    return StreamSupport.stream(managerMenu.spliterator(), false)
        .anyMatch(component -> getUI().getNavigator().getState().equals(component.getId()));
  }
}
