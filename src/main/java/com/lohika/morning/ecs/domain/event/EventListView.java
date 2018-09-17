package com.lohika.morning.ecs.domain.event;

import com.vaadin.data.provider.GridSortOrderBuilder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.Renderer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import static com.lohika.morning.ecs.utils.EcsUtils.formatString;

@SpringView(name = EventListView.VIEW_NAME)
@Slf4j
public class EventListView extends VerticalLayout implements View {

  public static final String VIEW_NAME = "";

  @Autowired
  private EventService eventService;

  private final Grid<MorningEvent> grid = new Grid<>(MorningEvent.class);
  private final Button buttonNew = new Button("New event", VaadinIcons.PLUS);
  private final TextField nameFilter = new TextField();
  private final CheckBox hidePastFilter = new CheckBox("Hide past", false);
  private final CheckBox hideCompletedFilter = new CheckBox("Hide completed", true);

  public EventListView() {
    grid.setSizeFull();
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    grid.setColumns("eventNumber", "name", "description", "date");
    grid.getColumn("eventNumber").setCaption("Event #").setMaximumWidth(100);
    grid.getColumn("description").setMaximumWidth(800);
    grid.getColumn("description").setRenderer((Renderer) new HtmlRenderer());
    grid.setSortOrder(new GridSortOrderBuilder<MorningEvent>().thenDesc(grid.getColumn("date")).build());

    nameFilter.setPlaceholder("Filter by name");
    nameFilter.setValueChangeMode(ValueChangeMode.LAZY);
  }

  @PostConstruct
  void init() {
    addListeners();
    final HorizontalLayout actions = new HorizontalLayout(buttonNew, nameFilter, hidePastFilter, hideCompletedFilter);
    addComponents(actions, grid);
    listEvents();
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {
    listEvents();
  }

  private void addListeners() {
    // Connect selected MorningEvent to editor or hide if none is selected
    grid.asSingleSelect()
        .addValueChangeListener(selectRowEvent -> navigateTo(EventDetailsView.VIEW_NAME, selectRowEvent.getValue().getEventNumber()));

    buttonNew.addClickListener(clickEvent -> navigateTo(EventEditorView.VIEW_NAME));

    // Replace listing with filtered content when user changes filter
    nameFilter.addValueChangeListener(e -> listEvents());
    hidePastFilter.addValueChangeListener(e -> listEvents());
    hideCompletedFilter.addValueChangeListener(e -> listEvents());
  }

  private void navigateTo(String viewName) {
    getUI().getNavigator().navigateTo(viewName);
  }

  private void navigateTo(String viewName, int id) {
    getUI().getNavigator().navigateTo(formatString("{}/{}", viewName, id));
  }

  private void listEvents() {
    grid.setItems(eventService.getEvents(nameFilter.getValue(), hidePastFilter.getValue(), hideCompletedFilter.getValue()));
  }

}
