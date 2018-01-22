package com.lohika.morning.ecs.domain.event;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@SpringUI(path = "/events")
@Theme("valo")
public class EventUi extends UI {
    private EventRepository eventRepository;
    private Grid<com.lohika.morning.ecs.domain.event.Event> grid;

    private final EventEditor editor;
    private final Button buttonNew = new Button("New event", VaadinIcons.PLUS);
    private final TextField filter = new TextField();

    @Autowired
    public EventUi(EventRepository eventRepository, EventEditor editor) {
        this.eventRepository = eventRepository;
        this.editor = editor;
        this.grid = new Grid<>(com.lohika.morning.ecs.domain.event.Event.class);
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setColumns("name", "description", "date");
        filter.setPlaceholder("Filter by name");
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        // Hook logic to components

        // Connect selected Event to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> editor.editEvent(Optional.ofNullable(e.getValue())));

        // Listen changes made by the editor, refresh data from backend
        editor.addHideListener(hideEvent -> listEvents());

        buttonNew.addClickListener(ce -> editor.editEvent(Optional.of(com.lohika.morning.ecs.domain.event.Event.builder().build())));

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listEvents(e.getValue()));

        // build layout
        final HorizontalLayout actions = new HorizontalLayout(buttonNew, filter);

        final VerticalLayout root = new VerticalLayout(actions, grid);

//        root.setSizeFull();
        addWindow(editor);
        setContent(root);

        // Initialize listing
        listEvents();
    }

    private void listEvents() {
        grid.setItems(eventRepository.findAll());
    }

    private void listEvents(String filterText) {
        grid.setItems(eventRepository.findByNameContainsIgnoreCase(filterText));
    }
}