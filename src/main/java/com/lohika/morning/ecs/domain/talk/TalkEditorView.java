package com.lohika.morning.ecs.domain.talk;

import com.lohika.morning.ecs.domain.event.EventDetailsView;
import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.lohika.morning.ecs.domain.speaker.SpeakersList;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static com.lohika.morning.ecs.utils.EcsUtils.formatString;

@SpringView(name = TalkEditorView.VIEW_NAME)
@Slf4j
public class TalkEditorView extends HorizontalLayout implements View {
  public static final String VIEW_NAME = "editTalk";

  private final TalkService talkService;

  /* Fields to edit properties in MorningEvent entity */
  private TextField title = new TextField("Title");
  private RichTextArea theses = new RichTextArea("Theses");

  /* Action buttons */
  private Button saveBtn = new Button("Save", VaadinIcons.CHECK);
  private Button cancelBtn = new Button("Cancel");
  private HorizontalLayout actions = new HorizontalLayout(saveBtn, cancelBtn);

  private SpeakersList speakersList;

  private final FormLayout editForm = new FormLayout();
  private final VerticalLayout speakersPanel;

  private Binder<Talk> binder = new BeanValidationBinder<>(Talk.class);

  @Autowired
  public TalkEditorView(TalkService talkService, SpeakersList speakersList) {
    this.talkService = talkService;
    editForm.addComponents(title, theses, actions);

    this.speakersList = speakersList;
    speakersPanel = new VerticalLayout(speakersList);
    addComponents(editForm, speakersPanel);

    // bind fields using naming convention
    binder.bindInstanceFields(this);

    // set button styles
    saveBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
    saveBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

    cancelBtn.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    Talk talk = talkService.getTalk(Long.parseLong(viewChangeEvent.getParameters()));

    binder.setBean(talk);
    speakersList.displaySpeakers(talk);

    // set button states
    final boolean isEventEditable = talk.getEvent().getDate().isAfter(LocalDate.now());
    saveBtn.setEnabled(isEventEditable);

    // add listeners
    saveBtn.addClickListener(clickEvent -> {
      talkService.save(talk);
      navigateToEventsDetails(talk.getEvent());
    });

    cancelBtn.addClickListener(clickEvent -> navigateToEventsDetails(talk.getEvent()));

  }

  private void navigateToEventsDetails(MorningEvent morningEvent) {
    getUI().getNavigator().navigateTo(formatString("{}/{}", EventDetailsView.VIEW_NAME, morningEvent.getEventNumber()));
  }

}
