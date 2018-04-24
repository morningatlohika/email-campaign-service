package com.lohika.morning.ecs.domain.campaign;

import com.lohika.morning.ecs.domain.email.EmailService;
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

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = CampaignPreviewView.VIEW_NAME)
public class CampaignPreviewView extends HorizontalLayout implements View {
  public static final String VIEW_NAME = "campaignPreview";

  private final EcsLabel subject = new EcsLabel("Subject");
  private final EcsLabel body = new EcsLabel("Body");

  private final Binder<Campaign> binder = new BeanValidationBinder<>(Campaign.class);

  private final Button editButton = new Button("Edit", VaadinIcons.EDIT);
  private final Button sendButton = new Button("Send", VaadinIcons.ANGLE_DOUBLE_RIGHT);
  private final Button cancelButton = new Button("Cancel");

  private final CampaignPreviewService campaignPreviewService;

  private final EmailService emailService;

  @PostConstruct
  public void init() {
    HorizontalLayout actions = new HorizontalLayout(editButton, sendButton, cancelButton);
    FormLayout form = new FormLayout(subject, body, actions);
    addComponents(form);
    body.setContentMode(ContentMode.HTML);

    editButton.addClickListener(this::editCampaign);
    cancelButton.addClickListener(this::cancelCampaign);
    sendButton.addClickListener(this::generateEmails);
  }

  private void generateEmails(Button.ClickEvent clickEvent) {
    emailService.compileEmails(binder.getBean().getId());
  }

  private void editCampaign(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(CampaignEditView.VIEW_NAME + "/" + binder.getBean().getId());
  }

  private void cancelCampaign(Button.ClickEvent clickEvent) {
    getUI().getNavigator().navigateTo(CampaignListView.VIEW_NAME);
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    Campaign campaign = getCampaign(viewChangeEvent);
    binder.setBean(campaign);
    binder.bindInstanceFields(this);
  }

  private Campaign getCampaign(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    if (StringUtils.EMPTY.equals(viewChangeEvent.getParameters())) {
      throw new RuntimeException("Id not found");
    }
    Long id = Long.valueOf(viewChangeEvent.getParameters());
    return campaignPreviewService.findOne(id);
  }
}
