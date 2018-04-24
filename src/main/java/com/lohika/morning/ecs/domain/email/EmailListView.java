package com.lohika.morning.ecs.domain.email;

import com.vaadin.data.HasValue;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@SpringView(name = EmailListView.VIEW_NAME)
public class EmailListView extends VerticalLayout implements View {
  public static final String VIEW_NAME = "emails";

  private final Grid<Email> grid = new Grid<>(Email.class);
  private final TextField filterTextField = new TextField();

  private final EmailService emailService;

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
    grid.setColumns("campaign.event.name", "campaign.name", "to", "subject", "body", "sent", "generatedAt", "sentAt");
    grid.getColumn("campaign.event.name").setCaption("Event");

    grid.setItems(emailService.findAll());
    grid.setSizeFull();
  }

  private void filterBy(HasValue.ValueChangeEvent<String> e) {
    grid.setItems(emailService.filterBy(e.getValue()));
  }
}
