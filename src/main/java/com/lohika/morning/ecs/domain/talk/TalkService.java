package com.lohika.morning.ecs.domain.talk;

import com.lohika.morning.ecs.domain.event.EventRepository;
import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.lohika.morning.ecs.service.GoogleSheetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class TalkService {

    public final EventRepository eventRepository;
    public final TalkRepository talkRepository;
    public final GoogleSheetsService googleSheetsService;

    @Autowired
    public TalkService(EventRepository eventRepository, TalkRepository talkRepository, GoogleSheetsService googleSheetsService) {
        this.eventRepository = eventRepository;
        this.talkRepository = talkRepository;
        this.googleSheetsService = googleSheetsService;
    }

    public void importTalks(MorningEvent event) {
        List<Talk> allTalks = googleSheetsService.getTalks(eventRepository.findAll());
        List<Talk> talksToSave = allTalks.stream().filter(t -> t.getEvent().getEventNumber() == event.getEventNumber()).collect(toList());
        talkRepository.save(talksToSave);
    }
}
