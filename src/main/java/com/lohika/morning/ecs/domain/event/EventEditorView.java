package com.lohika.morning.ecs.domain.event;

import lombok.extern.slf4j.Slf4j;

import com.lohika.morning.ecs.domain.talk.TalkService;
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
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static com.lohika.morning.ecs.utils.EcsUtils.formatString;

@SpringView(name = EventEditorView.VIEW_NAME)
@Slf4j
public class EventEditorView extends HorizontalLayout implements View {
  public static final String VIEW_NAME = "editEvent";

  private final EventService eventService;
  private final FormLayout editForm;
  /* Fields to edit properties in MorningEvent entity */
  private TextField eventNumber = new TextField("Event number");
  private TextField name = new TextField("Name");
  private RichTextArea description = new RichTextArea("Description");
  private DateField date = new DateField("Date");
  private TextField ticketsUrl = new TextField("Tickets URL");
  /* Action buttons */
  private Button saveBtn = new Button("Save", VaadinIcons.CHECK);
  private Button cancelBtn = new Button("Cancel");
  private HorizontalLayout actions = new HorizontalLayout(saveBtn, cancelBtn);
  private Binder<MorningEvent> binder = new BeanValidationBinder<>(MorningEvent.class);

  @Autowired
  public EventEditorView(EventService eventService, TalkService talkService) {
    this.eventService = eventService;

    editForm = new FormLayout(eventNumber, name, description, date, ticketsUrl, actions);

    addComponents(editForm);
    setWidth(100, Unit.PERCENTAGE);

    binder.forMemberField(eventNumber).withConverter(new StringToIntegerConverter("Please enter a number"));
    binder.bindInstanceFields(this);

    // set button styles
    saveBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
    saveBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

    cancelBtn.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    MorningEvent morningEvent = initMorningEvent(viewChangeEvent);
    binder.setBean(morningEvent);

    // set button states
    final boolean isEventEditable = !morningEvent.isCompleted();
    saveBtn.setEnabled(isEventEditable);

    // add listeners
    saveBtn.addClickListener(clickEvent -> {
      if (binder.validate().isOk()) {
        eventService.save(morningEvent);
        navigateTo(EventDetailsView.VIEW_NAME, morningEvent.getEventNumber());
      }
    });

    cancelBtn.addClickListener(clickEvent -> {
      if (binder.getBean().getId() == null) {
        getUI().getNavigator().navigateTo(EventListView.VIEW_NAME);
      } else {
        navigateTo(EventDetailsView.VIEW_NAME, morningEvent.getEventNumber());
      }
    });
  }

  private void navigateTo(String viewName, int id) {
    getUI().getNavigator().navigateTo(formatString("{}/{}", viewName, id));
  }

  private MorningEvent initMorningEvent(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    if (StringUtils.EMPTY.equals(viewChangeEvent.getParameters())) {
      return eventService.newEvent();
    }

    // TODO: handle exceptions (invalid ID etc.)
    int morningEventNumber = Integer.parseInt(viewChangeEvent.getParameters());
    return eventService.getEventByNumber(morningEventNumber);
  }

}
