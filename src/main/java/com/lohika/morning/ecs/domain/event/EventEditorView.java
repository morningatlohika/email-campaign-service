package com.lohika.morning.ecs.domain.event;

import com.lohika.morning.ecs.domain.talk.TalkService;
import com.lohika.morning.ecs.domain.talk.TalksList;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@SpringView(name = EventEditorView.VIEW_NAME)
@Slf4j
public class EventEditorView extends HorizontalLayout implements View {
    public static final String VIEW_NAME = "editEvent";

    private final EventService eventService;
    private final TalkService talkService;

    /* Fields to edit properties in MorningEvent entity */
    private TextField eventNumber = new TextField("Event number");
    private TextField name = new TextField("Name");
    private TextArea description = new TextArea("Description");
    private DateField date = new DateField("Date");
    private TextField ticketsUrl = new TextField("Tickets URL");

    /* Action buttons */
    private Button saveBtn = new Button("Save", VaadinIcons.CHECK);
    private Button cancelBtn = new Button("Cancel");
    private Button deleteBtn = new Button("Delete", VaadinIcons.TRASH);
    private Button importTalksBtn = new Button("Import Talks and Speakers", VaadinIcons.ARROW_DOWN);
    private HorizontalLayout actions = new HorizontalLayout(saveBtn, deleteBtn, cancelBtn);

    private TalksList talksList;
    private final FormLayout editForm = new FormLayout();
    private final VerticalLayout talksPanel;

    private Binder<MorningEvent> binder = new BeanValidationBinder<>(MorningEvent.class);

    @Autowired
    public EventEditorView(EventService eventService, TalkService talkService) {
        this.eventService = eventService;
        this.talkService = talkService;
        editForm.addComponents(eventNumber, name,description, date, ticketsUrl, actions);

        talksList = new TalksList(talkService);
        talksPanel = new VerticalLayout(talksList, importTalksBtn);
        addComponents(editForm, talksPanel);

        binder.forMemberField(eventNumber).withConverter(new StringToIntegerConverter("Please enter a number"));
        // bind remaining fields using naming convention
        binder.bindInstanceFields(this);

        // set button styles
        saveBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
        saveBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        cancelBtn.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);

        // static listener for Cancel button
        cancelBtn.addClickListener(clickEvent -> navigateToEventsList());
    }

    @PostConstruct
    void init() {
        log.info("========> EventEditorView @PostConstruct");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        MorningEvent morningEvent = initMorningEvent(viewChangeEvent);
        binder.setBean(morningEvent);

        // set button states
        final boolean isEventEditable = morningEvent.getDate().isAfter(LocalDate.now());
        saveBtn.setEnabled(isEventEditable);
        deleteBtn.setEnabled(isEventEditable);

        // add listeners
        saveBtn.addClickListener(clickEvent -> {
            eventService.save(morningEvent);
            navigateToEventsList();
        });

        deleteBtn.addClickListener(clickEvent -> {
            eventService.delete(morningEvent);
            navigateToEventsList();
        });

        importTalksBtn.addClickListener(clickEvent -> {
            talkService.importTalks(morningEvent);
            talksList.displayTalks(morningEvent);
        });

    }

    private void navigateToEventsList() {
        getUI().getNavigator().navigateTo(EventListView.VIEW_NAME);
    }

    private MorningEvent initMorningEvent(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if (StringUtils.EMPTY.equals(viewChangeEvent.getParameters())) {
            return eventService.newEvent();
        }

        // TODO: handle exceptions (invalid ID etc.)
        Long morningEventId = Long.parseLong(viewChangeEvent.getParameters());
        return eventService.getEvent(morningEventId);
    }

}
