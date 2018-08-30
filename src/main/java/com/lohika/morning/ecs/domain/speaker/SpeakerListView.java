package com.lohika.morning.ecs.domain.speaker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.vaadin.data.HasValue;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = SpeakerListView.VIEW_NAME)
public class SpeakerListView extends VerticalLayout implements View {
  public static final String VIEW_NAME = "speakers";

  private final Grid<Speaker> grid = new Grid<>(Speaker.class);
  private final TextField filterTextField = new TextField();

  private final SpeakerService speakerService;

  @PostConstruct
  void init() {
    HorizontalLayout header = new HorizontalLayout(filterTextField);
    header.setSizeFull();
    addComponents(header, grid);

    filterTextField.addValueChangeListener(this::filterBy);
    filterTextField.setPlaceholder("Filter by first name, or last name");
    filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
    filterTextField.setSizeFull();

    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    grid.setColumns("firstName", "lastName", "email", "company", "position", "city");

    grid.setItems(speakerService.findAll());
    grid.setSizeFull();
  }

  private void filterBy(HasValue.ValueChangeEvent<String> e) {
    grid.setItems(speakerService.filterBy(e.getValue()));
  }
}
