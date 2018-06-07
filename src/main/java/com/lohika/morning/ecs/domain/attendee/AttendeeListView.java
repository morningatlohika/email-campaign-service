package com.lohika.morning.ecs.domain.attendee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.lohika.morning.ecs.domain.applicationstatus.ApplicationStateService;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.FileDownloader;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = AttendeeListView.VIEW_NAME)
public class AttendeeListView extends VerticalLayout implements View {
  public static final String VIEW_NAME = "attendees";

  private final Button reloadButton = new Button("Reload emails", VaadinIcons.REFRESH);
  private final Button downloadButton = new Button("Download emails", VaadinIcons.DOWNLOAD);
  private final Label lastUpdateLabel = new Label("time of last update");
  private final TextField filterTextField = new TextField();
  private final Grid<Attendee> grid = new Grid<>(Attendee.class);

  private final AttendeeService attendeeService;
  private final AttendeeResourceService attendeeResourceService;
  private final ApplicationStateService applicationStateService;

  @PostConstruct
  void init() {
    HorizontalLayout header = new HorizontalLayout(reloadButton, downloadButton, lastUpdateLabel, filterTextField);
    header.setSizeFull();
    addComponents(header, grid);

    reloadButton.addClickListener(this::reloadAttendees);

    FileDownloader fileDownloader = new FileDownloader(attendeeResourceService.getResource());
    fileDownloader.extend(downloadButton);

    lastUpdateLabel.setCaption(applicationStateService.getUpdateAttendee());

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
    lastUpdateLabel.setCaption(applicationStateService.getUpdateAttendee());
  }
}
