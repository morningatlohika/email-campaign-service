package com.lohika.morning.ecs.domain.event;

import com.lohika.morning.ecs.domain.talk.TalkService;
import com.lohika.morning.ecs.domain.talk.TalksList;
import com.lohika.morning.ecs.vaadin.MorningPopup;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@SpringComponent
@UIScope
public class EventEditor extends MorningPopup {
    private static final long serialVersionUID = 1L;

    private final EventRepository repository;
    private final TalkService talkService;

    /**
     * The currently edited event
     */
    private MorningEvent event;

    /* Fields to edit properties in MorningEvent entity */
    private TextField eventNumber = new TextField("Event number");
    private TextField name = new TextField("Name");
    private TextArea description = new TextArea("Description");
    private DateField date = new DateField("Date");
    private TextField ticketsUrl = new TextField("Tickets URL");

    /* Action buttons */
    private Button save = new Button("Save", VaadinIcons.CHECK);
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcons.TRASH);
    private HorizontalLayout actions = new HorizontalLayout(save, delete, cancel);

    private TalksList talksList;
    private Button buttonImportTalks = new Button("Import Talks and Speakers", VaadinIcons.ARROW_DOWN);

    private Binder<MorningEvent> binder = new BeanValidationBinder<>(MorningEvent.class);

    @Autowired
    public EventEditor(EventRepository repository, TalkService talkService, TalksList talksList) {
        super("Edit MorningEvent");
        this.repository = repository;
        this.talkService = talkService;
        this.talksList = talksList;

        FormLayout editForm = new FormLayout(eventNumber, name, description, date, ticketsUrl, actions);
        //editForm.setWidth(50, Unit.PERCENTAGE);
        editForm.setWidthUndefined();

        VerticalLayout talksPanel = new VerticalLayout(talksList, buttonImportTalks);
        talksPanel.setComponentAlignment(buttonImportTalks, Alignment.BOTTOM_RIGHT);
        //talksPanel.setWidth(500, Unit.PIXELS);
        talksPanel.setWidthUndefined();

        HorizontalLayout container = new HorizontalLayout(editForm, talksPanel);
        container.setExpandRatio(editForm, 1);
        container.setExpandRatio(talksPanel, 1);
        container.setSpacing(true);
        setContent(container);

        binder.forMemberField(eventNumber).withConverter(new StringToIntegerConverter("Please enter a number"));
        // bind remaining fields using naming convention
        binder.bindInstanceFields(this);

        name.setWidth(100, Unit.PERCENTAGE);
        ticketsUrl.setWidth(100, Unit.PERCENTAGE);

        description.setWidth(100, Unit.PERCENTAGE);
        description.setHeight(200, Unit.PIXELS);

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        cancel.addClickListener(clickEvent -> this.hide());
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);

        this.setWidth(90, Unit.PERCENTAGE);
        this.hide();
    }

    public final void editEvent(MorningEvent event) {
        if (event == null) {
            this.hide();
            return;
        }

        final boolean persisted = event.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            this.event = repository.findOne(event.getId());
        } else {
            this.event = event;
        }

        final boolean isEventEditable = this.event.getDate().isAfter(LocalDate.now());
        save.setEnabled(isEventEditable);
        delete.setEnabled(isEventEditable);

        talksList.displayTalks(this.event);

        save.addClickListener(clickEvent -> {
            repository.save(this.event);
            this.hide();
        });

        delete.addClickListener(clickEvent -> {
            repository.delete(this.event);
            this.hide();
        });

        buttonImportTalks.addClickListener(clickEvent -> {
            talkService.importTalks(this.event);
            talksList.displayTalks(this.event);
        });

        // Bind event properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(this.event);

        this.show();

        // A hack to ensure the whole form is visible
        cancel.focus();

        // Select all text in name field automatically
        name.selectAll();
    }
}