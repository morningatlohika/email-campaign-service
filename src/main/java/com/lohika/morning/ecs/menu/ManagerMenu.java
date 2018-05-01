package com.lohika.morning.ecs.menu;

import com.lohika.morning.ecs.domain.event.EventDetailsView;
import com.lohika.morning.ecs.domain.event.EventListView;
import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;

import java.util.List;

public class ManagerMenu extends BaseMenu {

  public ManagerMenu(UI ui, List<MorningEvent> events) {
    super(ui);
    addComponent(createNavigationButton("Events", EventListView.VIEW_NAME));

    events.forEach(morningEvent -> {
      String viewName = EventDetailsView.VIEW_NAME + "/" + morningEvent.getEventNumber();
      Button button = createNavigationButton(morningEvent.getName(), viewName);
      addComponent(button);
    });
  }
}
