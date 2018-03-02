package com.lohika.morning.ecs.domain.unsubscribe;

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
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = UnsubscribeListView.VIEW_NAME)
public class UnsubscribeListView extends VerticalLayout implements View {
  public static final String VIEW_NAME = "unsubscribe";

  private final Button addButton = new Button("Add Unsubscribe", VaadinIcons.PLUS);
  private final TextField filterTextField = new TextField();
  private final Grid<Unsubscribe> grid = new Grid<>(Unsubscribe.class);

  private final UnsubscribeService unsubscribeService;

  @PostConstruct
  void init() {
    HorizontalLayout header = new HorizontalLayout(addButton, filterTextField);
    header.setSizeFull();
    addComponents(header, grid);

    addButton.addClickListener(this::createUnsubscribe);

    filterTextField.setPlaceholder("Filter by email");
    filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
    filterTextField.addValueChangeListener(this::filterBy);
    filterTextField.setSizeFull();

    grid.setItems(unsubscribeService.findAll());
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    grid.setColumns("email");
    grid.addColumn(unsubscribe -> "X", getButtonRenderer()).setWidth(70);
    grid.setSizeFull();
  }

  private ButtonRenderer<Unsubscribe> getButtonRenderer() {
    return new ButtonRenderer<>(this::deleteById);
  }

  private void deleteById(ClickableRenderer.RendererClickEvent<Unsubscribe> clickEvent) {
    unsubscribeService.delete(clickEvent.getItem());
    grid.setItems(unsubscribeService.findAll());
  }

  private void filterBy(HasValue.ValueChangeEvent<String> e) {
    grid.setItems(unsubscribeService.filterBy(e.getValue()));
  }

  private void createUnsubscribe(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(UnsubscribeAddView.VIEW_NAME);
  }
}
