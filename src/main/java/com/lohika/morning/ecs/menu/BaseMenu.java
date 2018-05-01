package com.lohika.morning.ecs.menu;

import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

public abstract class BaseMenu extends CssLayout {
  private final UI ui;

  public BaseMenu(UI ui) {
    this.ui = ui;
    this.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
  }

  protected Button createNavigationButton(String caption, final String viewName) {
    Button button = new Button(caption);
    button.setId(viewName);
    // If you didn't choose Java 8 when creating the project, convert this
    // to an anonymous listener class
    button.addClickListener(event -> {
      this.forEach(component -> component.removeStyleName(ValoTheme.BUTTON_PRIMARY));
      event.getButton().addStyleName(ValoTheme.BUTTON_PRIMARY);
      ui.getNavigator().navigateTo(viewName);
    });
    return button;
  }

  @Override
  public void setVisible(boolean visible) {
    if (visible) {
      this.forEach(component -> {
        component.setStyleName(null);
        if (getUI().getNavigator().getState().equals(component.getId())) {
          component.addStyleName(ValoTheme.BUTTON_PRIMARY);
        }
      });

    }
    super.setVisible(visible);
  }
}
