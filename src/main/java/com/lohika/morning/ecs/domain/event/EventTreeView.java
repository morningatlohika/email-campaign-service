package com.lohika.morning.ecs.domain.event;

import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@SpringView(name = EventTreeView.VIEW_NAME)
@Slf4j
public class EventTreeView extends VerticalLayout implements View {

  public static final String VIEW_NAME = "eventsTree";

  @Autowired
  private EventRepository eventRepository;

  //private final Grid<MorningEvent> grid = new Grid<>(MorningEvent.class);
  private final Button buttonNew = new Button("New event", VaadinIcons.PLUS);
  private final TextField filter = new TextField();

  private final Tree tree = new Tree();

  public EventTreeView() {
//        grid.setSizeFull();
//        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
//        grid.setColumns("eventNumber", "name", "description", "date");
//        grid.getColumn("eventNumber").setCaption("Event #").setMaximumWidth(100);
//        grid.getColumn("description").setMaximumWidth(800);
//        grid.setSortOrder(new GridSortOrderBuilder<MorningEvent>().thenDesc(grid.getColumn("date")).build());


    filter.setPlaceholder("Filter by name");
    filter.setValueChangeMode(ValueChangeMode.LAZY);
  }

  public EventTreeView(EventRepository eventRepository, Grid<MorningEvent> grid, Component... children) {
    super(children);
    this.eventRepository = eventRepository;
  }

  private static TreeNode<MorningEvent> wrap(MorningEvent e) {
    return new TreeNode<MorningEvent>(e) {
      @Override
      public String getLabel() {
        return this.getEntity().getName();
      }
    };
  }

  @PostConstruct
  void init() {
    addListeners();
    final HorizontalLayout actions = new HorizontalLayout(buttonNew, filter);
    addComponents(actions, tree);
//        listEvents();
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {
    TreeData<Labelable> eventsData = new TreeData<>();
    List<Labelable> treeNodes = eventRepository.findAll().stream().map(EventTreeView::wrap).collect(Collectors.toList());

    eventsData.addRootItems(treeNodes);

    tree.setDataProvider(new TreeDataProvider<>(eventsData));

    //listEvents();
  }

  private void addListeners() {
    // Connect selected MorningEvent to editor or hide if none is selected
//        grid.asSingleSelect()
//                .addValueChangeListener(selectRowEvent -> getUI().getNavigator().navigateTo(EventEditorView.VIEW_NAME + "/" + selectRowEvent.getValue().getId()));

    buttonNew.addClickListener(clickEvent -> getUI().getNavigator().navigateTo(EventEditorView.VIEW_NAME));

    // Replace listing with filtered content when user changes filter
//        filter.addValueChangeListener(e -> listEvents(e.getValue()));
  }

//    private void listEvents() {
//        grid.setItems(eventRepository.findAll());
//    }
//
//    private void listEvents(String filterText) {
//        grid.setItems(eventRepository.findByNameContainsIgnoreCase(filterText));
//    }


}

@FunctionalInterface
interface Labelable {
  String getLabel();
}

abstract class TreeNode<T> implements Labelable {
  private T entity;

  public TreeNode(T entity) {
    this.entity = entity;
  }

  T getEntity() {
    return this.entity;
  }

  @Override
  public String toString() {
    return getLabel();
  }
}


