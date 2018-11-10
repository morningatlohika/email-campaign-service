package com.lohika.morning.ecs.domain.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.lohika.morning.ecs.service.PublishCampaignService;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = EmailListView.VIEW_NAME)
public class EmailListView extends VerticalLayout implements View {
  public static final String VIEW_NAME = "emails";

  private final Grid<Email> grid = new Grid<>(Email.class);
  private final TextField filterTextField = new TextField();

  private final EmailService emailService;
  private final PublishCampaignService publishCampaignService;

  @PostConstruct
  void init() {
    HorizontalLayout header = new HorizontalLayout(filterTextField);
    header.setSizeFull();
    addComponents(header, grid);

    filterTextField.addValueChangeListener(this::filterBy);
    filterTextField.setPlaceholder("Filter by event name, campaign name, email, subject, or email body");
    filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
    filterTextField.setSizeFull();

    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    grid.setColumns("campaign.event.name", "campaign.name", "to", "subject", "generatedAt", "status",
        "lastSendingAttempt");
    grid.getColumn("campaign.event.name").setCaption("Event");

    grid.addComponentColumn(this::addAction).setCaption("Actions");

    grid.setStyleGenerator(email -> (Email.Status.FAILED == email.getStatus()) ? "failed" : "");

    grid.setItems(emailService.findAll());
    grid.setSizeFull();
  }

  private void filterBy(HasValue.ValueChangeEvent<String> e) {
    grid.setItems(emailService.filterBy(e.getValue()));
  }

  private Button addAction(Email email) {
    Button button = new Button(VaadinIcons.MAILBOX, e -> {
      publishCampaignService.reSend(email);
      e.getButton().setVisible(false);
      grid.setItems(emailService.findAll());
    });
    button.setVisible(Email.Status.FAILED == email.getStatus());

    return button;
  }
}
