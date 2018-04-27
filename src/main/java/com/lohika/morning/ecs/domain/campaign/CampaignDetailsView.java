package com.lohika.morning.ecs.domain.campaign;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.lohika.morning.ecs.vaadin.EcsLabel;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;

import static java.lang.Math.abs;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = CampaignDetailsView.VIEW_NAME)
public class CampaignDetailsView extends HorizontalLayout implements View {
  public static final String VIEW_NAME = "campaigDetailsn";

  private final EcsLabel name = new EcsLabel("Name");
  private final EcsLabel subject = new EcsLabel("Subject");
  private final EcsLabel body = new EcsLabel("Body");
  private final EcsLabel campaignPriority = new EcsLabel("Piority");
  private final EcsLabel campaignEmails = new EcsLabel("Emails");
  private final EcsLabel promoCode = new EcsLabel("Promo code");

  private final EcsLabel eventName = new EcsLabel("Event name");
  private final EcsLabel parentTemplateName = new EcsLabel("Parent template name");

  private final Button editButton = new Button("Edit", VaadinIcons.EDIT);
  private final Button previewButton = new Button("Preview", VaadinIcons.ANGLE_DOUBLE_RIGHT);
  private final Button deleteButton = new Button("Delete", VaadinIcons.TRASH);
  private final Button cancelButton = new Button("Cancel");
  private final Binder<Campaign> binder = new BeanValidationBinder<>(Campaign.class);

  private final CampaignService campaignService;

  @PostConstruct
  public void init() {

    HorizontalLayout actions = new HorizontalLayout(editButton, previewButton, deleteButton, cancelButton);

    FormLayout form = new FormLayout(name, subject, body, campaignEmails, promoCode, campaignPriority, actions);

    VerticalLayout details = new VerticalLayout(eventName, parentTemplateName);
    addComponents(form, details);
    body.setContentMode(ContentMode.HTML);

    binder.bindInstanceFields(this);

    editButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
    editButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    editButton.addClickListener(this::editCampaign);

    previewButton.addClickListener(this::previewCampaign);

    deleteButton.addClickListener(this::deleteCampaign);

    cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    cancelButton.addClickListener(this::cancelCampaign);
  }

  private String generatePriorityCaption(Integer i) {
    if (i == 0) {
      return "in event day";
    } else if (i % 7 == 0) {
      return abs(i / 7) + " week(s) " + ((i > 0) ? "after" : "before");
    }
    return abs(i) + " day(s) " + ((i > 0) ? "after" : "before");
  }

  private void editCampaign(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(CampaignEditView.VIEW_NAME + "/" + binder.getBean().getId());
  }

  private void previewCampaign(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(CampaignPreviewView.VIEW_NAME + "/" + binder.getBean().getId());
  }

  private void deleteCampaign(Button.ClickEvent clickEvent) {
    Campaign campaign = binder.getBean();
    campaignService.delete(campaign);
    getUI().getNavigator().navigateTo(CampaignListView.VIEW_NAME);
  }

  private void cancelCampaign(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(CampaignListView.VIEW_NAME);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    Campaign campaign = getCampaign(viewChangeEvent);
    campaignPriority.setValue(generatePriorityCaption(campaign.getPriority()));
    eventName.setValue(campaign.getEvent().getName());

    parentTemplateName.setVisible(campaign.getCampaignTemplate() != null);
    parentTemplateName.setValue(campaign.getCampaignTemplate() != null ? campaign.getCampaignTemplate().getName() : "");

    campaignEmails.setValue(campaign.isAttendee() ? "For all attendee" : campaign.getEmails());

    binder.setBean(campaign);
    boolean isEditable = campaign.getStatus() == Campaign.Status.NEW;
    editButton.setEnabled(isEditable);
    previewButton.setEnabled(isEditable);
    deleteButton.setEnabled(isEditable);
  }

  private Campaign getCampaign(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    if (StringUtils.EMPTY.equals(viewChangeEvent.getParameters())) {
      return campaignService.newCampaign();
    }
    Long id = Long.valueOf(viewChangeEvent.getParameters());
    return campaignService.findOne(id);
  }
}
