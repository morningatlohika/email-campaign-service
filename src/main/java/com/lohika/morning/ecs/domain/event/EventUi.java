package com.lohika.morning.ecs.domain.event;

import com.vaadin.annotations.Theme;
import com.vaadin.data.provider.GridSortOrderBuilder;
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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@SpringUI(path = "/events")
@Theme("valo")
public class EventUi extends UI {
    private EventRepository eventRepository;
    private Grid<MorningEvent> grid;

    private final EventEditor editor;
    private final Button buttonNew = new Button("New event", VaadinIcons.PLUS);
    private final TextField filter = new TextField();

    @Autowired
    public EventUi(EventRepository eventRepository, EventEditor editor) {
        this.eventRepository = eventRepository;
        this.editor = editor;
        this.grid = new Grid<>(MorningEvent.class);
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setColumns("eventNumber", "name", "description", "date");
        grid.getColumn("eventNumber").setCaption("Event #").setMaximumWidth(100);
        grid.getColumn("description").setMaximumWidth(800);
        grid.setSortOrder(new GridSortOrderBuilder<MorningEvent>().thenDesc(grid.getColumn("date")).build());

        filter.setPlaceholder("Filter by name");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        addListeners();

        final HorizontalLayout actions = new HorizontalLayout(buttonNew, filter);
        final VerticalLayout root = new VerticalLayout(actions, grid);

        addWindow(editor);
        setContent(root);

        listEvents();
    }

    private void addListeners() {
        // Connect selected MorningEvent to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> editor.editEvent(e.getValue()));

        // Listen changes made by the editor, refresh data from backend
        editor.addHideListener(hideEvent -> listEvents());

        buttonNew.addClickListener(ce -> editor.editEvent(MorningEvent.builder()
                .eventNumber(eventRepository.findMaxEventNumber() + 1)
                // set event date 2 weeks ahead by default
                .date(LocalDate.now().plus(2, ChronoUnit.WEEKS))
                .build()));

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