package com.lohika.morning.ecs.domain.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.lohika.morning.ecs.domain.campaign.AutoCampaignService;
import com.lohika.morning.ecs.domain.campaign.Campaign;
import com.lohika.morning.ecs.domain.campaign.CampaignDetailsView;
import com.lohika.morning.ecs.domain.campaign.CampaignEditView;
import com.lohika.morning.ecs.domain.campaign.CampaignListView;
import com.lohika.morning.ecs.domain.campaign.CampaignService;
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
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import static com.lohika.morning.ecs.utils.EcsUtils.formatString;
import static com.lohika.morning.ecs.utils.EcsUtils.isEditable;

import static java.util.stream.Collectors.toList;

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
  private final HorizontalLayout actions = new HorizontalLayout(editBtn, deleteBtn, closeBtn);

  /* Fields form */
  private final FormLayout eventDetails = new FormLayout(eventNumber, name, description, date, ticketsUrl, actions);

  /* Talks */
  private final Button importTalksBtn = new Button("Import Talks and Speakers", VaadinIcons.DOWNLOAD);
  private final Panel taskPanel = new Panel();
  private final VerticalLayout talksDetails = new VerticalLayout(importTalksBtn, taskPanel);

  /* Campaign */
  private final Button addCampaign = new Button("Add campaign", VaadinIcons.PLUS);
  private final Button autoCampaign = new Button("Auto provision campaign", VaadinIcons.MODAL_LIST);
  private final HorizontalLayout campaignActions = new HorizontalLayout(addCampaign, autoCampaign);

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

    addCampaign.addClickListener(this::createCampaign);
    campaignGrid.setColumns("name", "subject", "emails", "status");
    campaignGrid.asSingleSelect().addValueChangeListener(this::campaignDetails);
    campaignGrid.setSizeFull();
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    MorningEvent morningEvent = initMorningEvent(viewChangeEvent);
    binder.setBean(morningEvent);
    //talksList.displayTalks(morningEvent);

    // set button states
    final boolean isEventEditable = isEditable(morningEvent);
    final boolean isEventPersisted = morningEvent.getId() != null;
    editBtn.setEnabled(isEventEditable);
    deleteBtn.setEnabled(isEventPersisted && isEventEditable);
    importTalksBtn.setEnabled(isEventPersisted && isEventEditable);

    // add listeners
    editBtn.addClickListener(clickEvent -> {
      navigateTo(EventEditorView.VIEW_NAME, morningEvent.getEventNumber());
    });

    autoCampaign.addClickListener(clickEvent -> {
      autoCampaignService.autoProvisionCampaigns(morningEvent);
      reloadCampaigns();
    });

    deleteBtn.addClickListener(clickEvent -> {
      eventService.delete(morningEvent);
      navigateTo(EventListView.VIEW_NAME);
    });

    importTalksBtn.addClickListener(clickEvent -> {
      talkService.importTalks(morningEvent);
      reloadTasks();
    });

    reloadTasks();
    reloadCampaigns();
  }

  private void createCampaign(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(CampaignEditView.VIEW_NAME);
  }

  private void reloadTasks() {
    List<Talk> talks = talkService.getTalks(binder.getBean());
    Component[] taskComponents = talks.stream()
        .map(talk -> new TalkPanel(talk, isEditable(binder.getBean())))
        .toArray(Component[]::new);
    taskPanel.setContent(new VerticalLayout(taskComponents));
  }

  private void reloadCampaigns() {
    List<Campaign> campaigns = campaignService.findByEventId(binder.getBean());
    campaignGrid.setItems(campaigns);
    autoCampaign.setEnabled(campaigns.isEmpty());
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
