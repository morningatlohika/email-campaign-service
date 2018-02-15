package com.lohika.morning.ecs.domain.event;

import com.lohika.morning.ecs.domain.talk.TalkService;
import com.lohika.morning.ecs.domain.talk.TalksList;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

import static com.lohika.morning.ecs.utils.EcsUtils.formatString;

@SpringView(name = EventDetailsView.VIEW_NAME)
@Slf4j
public class EventDetailsView extends HorizontalLayout implements View {
    public static final String VIEW_NAME = "eventDetails";

    private final EventService eventService;
    private final TalkService talkService;

    /* Fields to edit properties in MorningEvent entity */
    private Label eventNumber = new Label("Event number");
    private Label name = new Label("Name");
    private Label description = new Label("Description");
    private Label date = new Label("Date");
    private Link ticketsUrl; //  = new Link("Tickets URL");

    /* Action buttons */
    private Button editBtn = new Button("Edit", VaadinIcons.EDIT);
    private Button closeBtn = new Button("Close", VaadinIcons.CLOSE);
    private Button deleteBtn = new Button("Delete", VaadinIcons.TRASH);
    private Button importTalksBtn = new Button("Import Talks and Speakers", VaadinIcons.ARROW_DOWN);
    private HorizontalLayout actions = new HorizontalLayout(editBtn, deleteBtn, closeBtn);

    private TalksList talksList;
    private final FormLayout eventDetails = new FormLayout();
    private final VerticalLayout talksPanel;

    @Autowired
    public EventDetailsView(EventService eventService, TalkService talkService) {
        this.eventService = eventService;
        this.talkService = talkService;

        ticketsUrl = new Link("Tickets URL", new ExternalResource("http://example.com"));
        eventDetails.addComponents(eventNumber, name,description, date, ticketsUrl, actions);

        talksList = new TalksList(talkService);
        talksPanel = new VerticalLayout(importTalksBtn, talksList);
        addComponents(eventDetails, talksPanel);

        // set button styles
        editBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
        editBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        closeBtn.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    }

    @PostConstruct
    void init() {
        log.info("========> EventEditorView @PostConstruct");
        // static listener for Cancel button
        closeBtn.addClickListener(clickEvent -> navigateTo(EventListView.VIEW_NAME));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        MorningEvent morningEvent = initMorningEvent(viewChangeEvent);
        talksList.displayTalks(morningEvent);

        // set button states
        final boolean isEventEditable = morningEvent.getDate().isAfter(LocalDate.now());
        final boolean isEventPersisted  = morningEvent.getId() != null;
        editBtn.setEnabled(isEventEditable);
        deleteBtn.setEnabled(isEventPersisted && isEventEditable);
        importTalksBtn.setEnabled(isEventPersisted && isEventEditable);

        // add listeners
        editBtn.addClickListener(clickEvent -> {
            navigateTo(EventEditorView.VIEW_NAME, morningEvent.getEventNumber());
        });

        deleteBtn.addClickListener(clickEvent -> {
            eventService.delete(morningEvent);
            navigateTo(EventListView.VIEW_NAME);
        });

        importTalksBtn.addClickListener(clickEvent -> {
            talkService.importTalks(morningEvent);
            talksList.displayTalks(morningEvent);
        });

    }

    private void navigateTo(String viewName) {
        getUI().getNavigator().navigateTo(viewName);
    }

    private void navigateTo(String viewName, int id) {
        getUI().getNavigator().navigateTo(formatString("{}/{}", viewName, id));
    }

    private MorningEvent initMorningEvent(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        // TODO: handle exceptions (invalid ID etc.)
        int morningEventNumber = Integer.parseInt(viewChangeEvent.getParameters());
        return eventService.getEventByNumber(morningEventNumber);
    }

}
