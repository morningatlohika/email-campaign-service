package com.lohika.morning.ecs.domain.campaigntemplate;

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
@SpringView(name = CampaignTemplateListView.VIEW_NAME)
public class CampaignTemplateListView extends VerticalLayout implements View {
  public static final String VIEW_NAME = "campaignTemplate";

  private final Grid<CampaignTemplate> grid = new Grid<>(CampaignTemplate.class);
  private final TextField filterTextField = new TextField();
  private final Button addButton = new Button("Add template", VaadinIcons.PLUS);

  private final CampaignTemplateService campaignTemplateService;

  @PostConstruct
  void init() {
    HorizontalLayout header = new HorizontalLayout(addButton, filterTextField);
    header.setSizeFull();
    addComponents(header, grid);

    addButton.addClickListener(this::createCampaignTemplate);

    filterTextField.addValueChangeListener(this::filterBy);
    filterTextField.setPlaceholder("Filter by name / subject / body / emails");
    filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
    filterTextField.setSizeFull();

    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    grid.setColumns("name", "subject", "emails");
    grid.setItems(campaignTemplateService.findAll());
    grid.asSingleSelect().addValueChangeListener(this::editCampaignTemplate);
    grid.addColumn(template -> "X", getButtonRenderer()).setWidth(70);
    grid.setSizeFull();
  }

  private ButtonRenderer<CampaignTemplate> getButtonRenderer() {
    return new ButtonRenderer<>(this::deleteById);
  }

  private void editCampaignTemplate(HasValue.ValueChangeEvent<CampaignTemplate> selectRowEvent) {
    getUI().getNavigator().navigateTo(CampaignTemplateEditView.VIEW_NAME + "/" + selectRowEvent.getValue().getId());
  }

  private void filterBy(HasValue.ValueChangeEvent<String> e) {
    grid.setItems(campaignTemplateService.filterBy(e.getValue()));
  }

  private void createCampaignTemplate(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(CampaignTemplateEditView.VIEW_NAME);
  }

  private void deleteById(ClickableRenderer.RendererClickEvent<CampaignTemplate> clickEvent) {
    campaignTemplateService.delete(clickEvent.getItem());
    grid.setItems(campaignTemplateService.findAll());
  }
}
