package com.lohika.morning.ecs.domain.attendee;

import lombok.extern.slf4j.Slf4j;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringView(name = AttendeeListView.VIEW_NAME)
@Slf4j
public class AttendeeListView extends VerticalLayout implements View {
  public static final String VIEW_NAME = "attendees";
  private final Grid<Attendee> grid = new Grid<>(Attendee.class);
  private final TextField filterTextField = new TextField();
  private final Button reloadButton = new Button("Re-load", VaadinIcons.PLUS);

  @Autowired
  private AttendeeService attendeeService;

  public AttendeeListView() {
    grid.setSizeFull();
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    grid.setColumns("firstName", "secondName", "email");

    filterTextField.setPlaceholder("Filter by first name / second name /email");
    filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
  }

  @PostConstruct
  void init() {
    filterTextField.addValueChangeListener(e ->
        grid.setItems(attendeeService.filterBy(e.getValue())));

    reloadButton.addClickListener(clickEvent -> {
      attendeeService.reLoad();
      getUI().getNavigator().navigateTo(AttendeeListView.VIEW_NAME);
    });

    final HorizontalLayout actions = new HorizontalLayout(reloadButton, filterTextField);
    addComponents(actions, grid);

    grid.setItems(attendeeService.findAll());
  }
}
