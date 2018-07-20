package com.lohika.morning.ecs.domain.campaign;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.lohika.morning.ecs.vaadin.EcsLabel;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;

import org.apache.commons.lang3.StringUtils;

import java.util.Set;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = CampaignPreviewView.VIEW_NAME)
public class CampaignPreviewView extends HorizontalLayout implements View {
  public static final String VIEW_NAME = "campaignPreview";

  private final EcsLabel subject = new EcsLabel("Subject");
  private final EcsLabel body = new EcsLabel("Body");
  private final EcsLabel warnings = new EcsLabel("Warnings");

  private final Binder<Campaign> binder = new BeanValidationBinder<>(Campaign.class);

  private final Button editButton = new Button("Edit", VaadinIcons.EDIT);
  private final Button publishButton = new Button("Publish", VaadinIcons.ANGLE_DOUBLE_RIGHT);
  private final Button cancelButton = new Button("Cancel");

  private final CampaignPreviewService campaignPreviewService;

  private final CampaignService campaignService;


  @PostConstruct
  public void init() {
    HorizontalLayout actions = new HorizontalLayout(editButton, publishButton, cancelButton);
    FormLayout form = new FormLayout(subject, body, warnings, actions);
    addComponents(form);
    body.setContentMode(ContentMode.HTML);

    editButton.addClickListener(this::editCampaign);
    cancelButton.addClickListener(this::cancelCampaign);
    publishButton.addClickListener(this::startCampaign);
  }

  private void startCampaign(Button.ClickEvent clickEvent) {
    publishButton.setEnabled(false);
    Campaign campaign = binder.getBean();
    campaignService.updateStatus(campaign, Campaign.Status.PENDING);
    getUI().getNavigator().navigateTo(CampaignDetailsView.VIEW_NAME + "/" + campaign.getId());
  }

  private void editCampaign(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(CampaignEditView.VIEW_NAME + "/" + binder.getBean().getId());
  }

  private void cancelCampaign(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(CampaignDetailsView.VIEW_NAME + "/" + binder.getBean().getId());
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    Campaign campaign = getCampaign(viewChangeEvent);
    binder.setBean(campaign);
    binder.bindInstanceFields(this);

    publishButton.setEnabled(campaign.isEditable());

    Long id = Long.valueOf(viewChangeEvent.getParameters());
    Set<String> unusedPlaceholders = campaignPreviewService.getUnusedPlaceholders(id);
    warnings.setVisible(!unusedPlaceholders.isEmpty());
    warnings.setValue("Following value is not set: " + String.join(", ", unusedPlaceholders));
  }

  private Campaign getCampaign(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    if (StringUtils.EMPTY.equals(viewChangeEvent.getParameters())) {
      throw new RuntimeException("Id not found");
    }
    Long id = Long.valueOf(viewChangeEvent.getParameters());
    return campaignPreviewService.findOne(id);
  }
}
