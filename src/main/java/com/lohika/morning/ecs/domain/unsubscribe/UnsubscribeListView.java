package com.lohika.morning.ecs.domain.unsubscribe;

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
import com.vaadin.ui.renderers.ButtonRenderer;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringView(name = UnsubscribeListView.VIEW_NAME)
@Slf4j
public class UnsubscribeListView extends VerticalLayout implements View {
  public static final String VIEW_NAME = "unsubscribe";
  private final Grid<Unsubscribe> grid = new Grid<>(Unsubscribe.class);
  private final TextField filterTextField = new TextField();
  private final Button addButton = new Button("Add", VaadinIcons.PLUS);

  @Autowired
  private UnsubscribeService unsubscribeService;

  public UnsubscribeListView() {
    grid.setSizeFull();
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    grid.setColumns("email");

    filterTextField.setPlaceholder("Filter by email");
    filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
  }

  @PostConstruct
  void init() {
    filterTextField.addValueChangeListener(e -> grid.setItems(unsubscribeService.filterByEmail(e.getValue())));

    addButton.addClickListener(clickEvent -> getUI().getNavigator().navigateTo(UnsubscribeAddView.VIEW_NAME));

    final HorizontalLayout actions = new HorizontalLayout(addButton, filterTextField);
    addComponents(actions, grid);

    grid.setItems(unsubscribeService.findAll());

    grid.addColumn(person -> "X",
        new ButtonRenderer(clickEvent -> {
          unsubscribeService.delete((Unsubscribe) clickEvent.getItem());
          getUI().getNavigator().navigateTo(UnsubscribeListView.VIEW_NAME);
        }));
  }
}
