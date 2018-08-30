package com.lohika.morning.ecs.domain.talk;

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
@SpringView(name = TalkListView.VIEW_NAME)
public class TalkListView extends VerticalLayout implements View {
  public static final String VIEW_NAME = "talks";

  private final Grid<Talk> grid = new Grid<>(Talk.class);
  private final TextField filterTextField = new TextField();

  private final TalkService talkService;

  @PostConstruct
  void init() {
    HorizontalLayout header = new HorizontalLayout(filterTextField);
    header.setSizeFull();
    addComponents(header, grid);

    filterTextField.addValueChangeListener(this::filterBy);
    filterTextField.setPlaceholder("Filter by talk title, or theses");
    filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
    filterTextField.setSizeFull();

    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    grid.setColumns("title", "language", "level", "targetAudience", "theses");

    grid.setItems(talkService.findAll());
    grid.setSizeFull();
  }

  private void filterBy(HasValue.ValueChangeEvent<String> e) {
    grid.setItems(talkService.filterBy(e.getValue()));
  }
}
