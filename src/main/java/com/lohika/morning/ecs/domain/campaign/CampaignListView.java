package com.lohika.morning.ecs.domain.campaign;

import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = CampaignListView.VIEW_NAME)
public class CampaignListView extends VerticalLayout implements View {
  public static final String VIEW_NAME = "campaign";

  private final Grid<Campaign> grid = new Grid<>(Campaign.class);
  private final TextField filterTextField = new TextField();
  private final Button addButton = new Button("Add campaign", VaadinIcons.PLUS);

  private final CampaignService campaignService;

  @PostConstruct
  void init() {
    HorizontalLayout header = new HorizontalLayout(addButton, filterTextField);
    header.setSizeFull();
    addComponents(header, grid);

    addButton.addClickListener(this::createCampaign);

    filterTextField.addValueChangeListener(this::filterBy);
    filterTextField.setPlaceholder("Filter by name / subject / body / emails");
    filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
    filterTextField.setSizeFull();

    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    grid.setColumns("event.name", "name", "subject", "emails", "status");
    grid.getColumn("event.name").setCaption("Event");

    grid.setItems(campaignService.findAll());
    grid.asSingleSelect().addValueChangeListener(this::editCampaign);
    grid.setSizeFull();
  }

  private void editCampaign(HasValue.ValueChangeEvent<Campaign> selectRowEvent) {
    getUI().getNavigator().navigateTo(CampaignEditView.VIEW_NAME + "/" + selectRowEvent.getValue().getId());
  }

  private void filterBy(HasValue.ValueChangeEvent<String> e) {
    grid.setItems(campaignService.filterBy(e.getValue()));
  }

  private void createCampaign(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(CampaignEditView.VIEW_NAME);
  }

}
