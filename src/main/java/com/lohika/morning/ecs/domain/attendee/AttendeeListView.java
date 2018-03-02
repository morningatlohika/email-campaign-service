package com.lohika.morning.ecs.domain.attendee;

import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = AttendeeListView.VIEW_NAME)
public class AttendeeListView extends VerticalLayout implements View {
  public static final String VIEW_NAME = "attendees";

  private final Button reloadButton = new Button("Reload emails", VaadinIcons.DOWNLOAD);
  private final TextField filterTextField = new TextField();
  private final Grid<Attendee> grid = new Grid<>(Attendee.class);

  private final AttendeeService attendeeService;

  @PostConstruct
  void init() {
    HorizontalLayout header = new HorizontalLayout(reloadButton, filterTextField);
    header.setSizeFull();
    addComponents(header, grid);

    reloadButton.addClickListener(this::reloadAttendees);

    filterTextField.setPlaceholder("Filter by first name / last name /email");
    filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
    filterTextField.addValueChangeListener(this::filterBy);
    filterTextField.setSizeFull();

    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    grid.setColumns("firstName", "lastName", "email");
    grid.setItems(attendeeService.findAll());
    grid.setSizeFull();
  }

  private void filterBy(HasValue.ValueChangeEvent<String> e) {
    grid.setItems(attendeeService.filterBy(e.getValue()));
  }

  private void reloadAttendees(Button.ClickEvent clickEvent) {
    attendeeService.reload();
    grid.setItems(attendeeService.findAll());
  }
}
