package com.lohika.morning.ecs.domain.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = TemplateListView.VIEW_NAME)
public class TemplateListView extends VerticalLayout implements View {
  public static final String VIEW_NAME = "template";

  private final Grid<Template> grid = new Grid<>(Template.class);
  private final TextField filterTextField = new TextField();
  private final Button addButton = new Button("Add template", VaadinIcons.PLUS);

  private final TemplateService templateService;

  @PostConstruct
  void init() {
    HorizontalLayout header = new HorizontalLayout(addButton, filterTextField);
    header.setSizeFull();
    addComponents(header, grid);

    addButton.addClickListener(this::createTemplate);

    filterTextField.addValueChangeListener(this::filterBy);
    filterTextField.setPlaceholder("Filter by name / subject / body / emails");
    filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
    filterTextField.setSizeFull();

    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    grid.setColumns("name", "subject", "emails");
    grid.setItems(templateService.findAll());
    grid.asSingleSelect().addValueChangeListener(this::editTemplate);
    grid.addColumn(template -> "X", getButtonRenderer()).setWidth(70);
    grid.setSizeFull();
  }

  private ButtonRenderer<Template> getButtonRenderer() {
    return new ButtonRenderer<>(this::deleteById);
  }

  private void editTemplate(HasValue.ValueChangeEvent<Template> selectRowEvent) {
    getUI().getNavigator().navigateTo(TemplateEditView.VIEW_NAME + "/" + selectRowEvent.getValue().getId());
  }

  private void filterBy(HasValue.ValueChangeEvent<String> e) {
    grid.setItems(templateService.filterByEmail(e.getValue()));
  }

  private void createTemplate(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(TemplateEditView.VIEW_NAME);
  }

  private void deleteById(ClickableRenderer.RendererClickEvent<Template> clickEvent) {
    templateService.delete(clickEvent.getItem());
    getUI().getNavigator().navigateTo(TemplateListView.VIEW_NAME);
  }
}
