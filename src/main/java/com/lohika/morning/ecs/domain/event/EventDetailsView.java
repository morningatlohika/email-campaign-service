package com.lohika.morning.ecs.domain.event;

import com.lohika.morning.ecs.domain.campaign.*;
import com.lohika.morning.ecs.domain.talk.Talk;
import com.lohika.morning.ecs.domain.talk.TalkPanel;
import com.lohika.morning.ecs.domain.talk.TalkService;
import com.lohika.morning.ecs.vaadin.EcsLabel;
import com.lohika.morning.ecs.vaadin.StringToLocalDateConverter;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.dialogs.ConfirmDialog;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.lohika.morning.ecs.utils.EcsUtils.formatString;

@SpringView(name = EventDetailsView.VIEW_NAME)
@Slf4j
@RequiredArgsConstructor
public class EventDetailsView extends VerticalLayout implements View {
  public static final String VIEW_NAME = "eventDetails";

  /* Fields to edit properties in MorningEvent entity */
  private final EcsLabel eventNumber = new EcsLabel("Event number");
  private final EcsLabel name = new EcsLabel("Name");
  private final EcsLabel description = new EcsLabel("Description");
  private final EcsLabel date = new EcsLabel("Date");
  private final EcsLabel ticketsUrl = new EcsLabel("Tickets URL", true);

  /* Action buttons */
  private final Button editBtn = new Button("Edit", VaadinIcons.EDIT);
  private final Button closeBtn = new Button("Close", VaadinIcons.CLOSE);
  private final Button deleteBtn = new Button("Delete", VaadinIcons.TRASH);
  private final Button wrapUpBtn = new Button("Wrap Up The Event", VaadinIcons.ARCHIVE);
  private final HorizontalLayout actions = new HorizontalLayout(editBtn, deleteBtn, closeBtn);

  /* Fields form */
  private final FormLayout eventDetails = new FormLayout(eventNumber, name, date, ticketsUrl, description, actions, wrapUpBtn);

  /* Talks */
  private final Button importTalksBtn = new Button("Import Talks and Speakers", VaadinIcons.DOWNLOAD);
  private final Panel taskPanel = new Panel();
  private final VerticalLayout talksDetails = new VerticalLayout(importTalksBtn, taskPanel);

  /* Campaign */
  private final Button addCampaignBtn = new Button("Add campaign", VaadinIcons.PLUS);
  private final Button autoCampaignBtn = new Button("Auto provision campaign", VaadinIcons.MODAL_LIST);
  private final HorizontalLayout campaignActions = new HorizontalLayout(addCampaignBtn, autoCampaignBtn);

  private final Grid<Campaign> campaignGrid = new Grid<>(Campaign.class);
  private final VerticalLayout campaignDetails = new VerticalLayout(campaignActions, campaignGrid);

  /* */
  private final Binder<MorningEvent> binder = new Binder<>(MorningEvent.class);

  private final EventService eventService;
  private final TalkService talkService;
  private final AutoCampaignService autoCampaignService;
  private final CampaignService campaignService;

  @PostConstruct
  void init() {
    HorizontalLayout top = new HorizontalLayout(eventDetails, talksDetails);
    addComponents(top, campaignDetails);

    setWidth(100, Unit.PERCENTAGE);

    binder.forMemberField(eventNumber).withConverter(new StringToIntegerConverter("Please enter a number"));
    binder.forMemberField(date).withConverter(new StringToLocalDateConverter());
    binder.bindInstanceFields(this);

    // set button styles
    editBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
    editBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

    closeBtn.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    closeBtn.addClickListener(clickEvent -> navigateTo(EventListView.VIEW_NAME));

    addCampaignBtn.addClickListener(this::createCampaign);
    campaignGrid.setColumns("name", "subject", "emails", "status");
    campaignGrid.asSingleSelect().addValueChangeListener(this::campaignDetails);
    campaignGrid.setSizeFull();

    description.setContentMode(ContentMode.HTML);
    description.setWidth(500, Unit.PIXELS);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    MorningEvent morningEvent = initMorningEvent(viewChangeEvent);
    binder.setBean(morningEvent);

    // set button states
    final boolean isEventEditable = !morningEvent.isCompleted();
    final boolean isEventPersisted = morningEvent.getId() != null;
    editBtn.setEnabled(isEventEditable);
    deleteBtn.setEnabled(isEventPersisted && isEventEditable);
    importTalksBtn.setEnabled(isEventPersisted && isEventEditable);
    addCampaignBtn.setEnabled(isEventPersisted && isEventEditable);
    autoCampaignBtn.setEnabled(isEventPersisted && isEventEditable);
    wrapUpBtn.setEnabled(isEventPersisted && isEventEditable);

    // add listeners
    editBtn.addClickListener(clickEvent -> {
      navigateTo(EventEditorView.VIEW_NAME, morningEvent.getEventNumber());
    });

    autoCampaignBtn.addClickListener(clickEvent -> {
      autoCampaignService.autoProvisionCampaigns(morningEvent);
      reloadCampaigns();
    });

    deleteBtn.addClickListener(clickEvent -> {
      eventService.delete(morningEvent);
      navigateTo(EventListView.VIEW_NAME);
    });

    wrapUpBtn.addClickListener(
        clickEvent -> {
          ConfirmDialog.show(getUI(), "Are you sure you want to wrap-up the event?",
              "If you wrap-up the event, it will become read only.",
              "Yes, please wrap-up",
              "No",
              dialog -> {
                if (dialog.isConfirmed()) {
                  eventService.wrapUp(morningEvent);
                  navigateTo(EventDetailsView.VIEW_NAME, morningEvent.getEventNumber());
                }
              }
          );
        }
    );

    importTalksBtn.addClickListener(clickEvent -> {
      if (talkService.talksExist(morningEvent)) {
        ConfirmDialog.show(getUI(), "Are you sure you want to re-import talks?",
            "If you re-import, all existing talks and speakers for given event will be overridden.",
            "Yes, please re-import",
            "No, please keep existing data",
            dialog -> {
              if (dialog.isConfirmed()) {
                talkService.reimportTalks(morningEvent);
              }
            }
        );
      } else {
        talkService.importTalks(morningEvent);
      }
      reloadTalks();
    });

    reloadTalks();
    reloadCampaigns();
  }

  private void createCampaign(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(CampaignEditView.VIEW_NAME_FOR_EVENT + binder.getBean().getEventNumber());
  }

  private void reloadTalks() {
    List<Talk> talks = talkService.getTalks(binder.getBean());
    Component[] taskComponents = talks.stream()
        .map(talk -> new TalkPanel(talk, !binder.getBean().isCompleted()))
        .toArray(Component[]::new);
    taskPanel.setContent(new VerticalLayout(taskComponents));
  }

  private void reloadCampaigns() {
    List<Campaign> campaigns = campaignService.findByEventId(binder.getBean());
    campaignGrid.setItems(campaigns);
    autoCampaignBtn.setEnabled(!binder.getBean().isCompleted() && campaigns.isEmpty());
  }

  private void campaignDetails(HasValue.ValueChangeEvent<Campaign> selectRowEvent) {
    getUI().getNavigator().navigateTo(CampaignDetailsView.VIEW_NAME + "/" + selectRowEvent.getValue().getId());
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
