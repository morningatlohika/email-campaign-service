package com.lohika.morning.ecs.domain.event;

import com.lohika.morning.ecs.vaadin.MorningPopup;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
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

    /**
     * The currently edited event
     */
    private MorningEvent event;

    /* Fields to edit properties in MorningEvent entity */
    private TextField name = new TextField("Name");
    private TextArea description = new TextArea("Description");
    private DateField date = new DateField("Date");
    private TextField ticketsUrl = new TextField("Tickets URL");

    /* Action buttons */
    private Button save = new Button("Save", VaadinIcons.CHECK);
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcons.TRASH);
    private HorizontalLayout actions = new HorizontalLayout(save, delete, cancel);

    private Binder<MorningEvent> binder = new BeanValidationBinder<>(MorningEvent.class);

    @Autowired
    public EventEditor(EventRepository repository) {
        super("Edit MorningEvent");
        this.repository = repository;

        VerticalLayout container = new VerticalLayout();
        setContent(container);

        FormLayout fl = new FormLayout(name, description, date, ticketsUrl, actions);
        container.addComponent(fl);
        container.setSpacing(true);

        // bind using naming convention
        binder.bindInstanceFields(this);

        name.setWidth(90, Unit.PERCENTAGE);
        ticketsUrl.setWidth(90, Unit.PERCENTAGE);

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        cancel.addClickListener(clickEvent -> this.hide());
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);

        description.setWidth(90, Unit.PERCENTAGE);
        description.setHeight(200, Unit.PIXELS);

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

        save.addClickListener(clickEvent -> {
            repository.save(this.event);
            this.hide();
        });

        delete.addClickListener(clickEvent -> {
            repository.delete(this.event);
            this.hide();
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