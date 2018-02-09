package com.lohika.morning.ecs.domain.event;

import com.vaadin.data.provider.GridSortOrderBuilder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringView(name = EventListView.VIEW_NAME)
@Slf4j
public class EventListView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "events";

    @Autowired
    private EventRepository eventRepository;

    private final Grid<MorningEvent> grid = new Grid<>(MorningEvent.class);
    private final Button buttonNew = new Button("New event", VaadinIcons.PLUS);
    private final TextField filter = new TextField();

    public EventListView() {
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setColumns("eventNumber", "name", "description", "date");
        grid.getColumn("eventNumber").setCaption("Event #").setMaximumWidth(100);
        grid.getColumn("description").setMaximumWidth(800);
        grid.setSortOrder(new GridSortOrderBuilder<MorningEvent>().thenDesc(grid.getColumn("date")).build());

        filter.setPlaceholder("Filter by name");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
    }

    public EventListView(EventRepository eventRepository, Grid<MorningEvent> grid, Component... children) {
        super(children);
        this.eventRepository = eventRepository;
    }

    @PostConstruct
    void init() {
        addListeners();
        final HorizontalLayout actions = new HorizontalLayout(buttonNew, filter);
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
                .addValueChangeListener(selectRowEvent -> getUI().getNavigator().navigateTo(EventEditorView.VIEW_NAME + "/" + selectRowEvent.getValue().getId()));

        buttonNew.addClickListener(clickEvent -> getUI().getNavigator().navigateTo(EventEditorView.VIEW_NAME));

        // Replace listing with filtered content when user changes filter
        filter.addValueChangeListener(e -> listEvents(e.getValue()));
    }

    private void listEvents() {
        grid.setItems(eventRepository.findAll());
    }

    private void listEvents(String filterText) {
        grid.setItems(eventRepository.findByNameContainsIgnoreCase(filterText));
    }

}
