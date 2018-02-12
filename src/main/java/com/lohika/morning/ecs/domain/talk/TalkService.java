package com.lohika.morning.ecs.domain.talk;

import com.lohika.morning.ecs.domain.event.EventRepository;
import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.lohika.morning.ecs.service.GoogleSheetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class TalkService {

    private final EventRepository eventRepository;
    private final TalkRepository talkRepository;
    private final GoogleSheetsService googleSheetsService;

    @Autowired
    public TalkService(EventRepository eventRepository, TalkRepository talkRepository, GoogleSheetsService googleSheetsService) {
        this.eventRepository = eventRepository;
        this.talkRepository = talkRepository;
        this.googleSheetsService = googleSheetsService;
    }

    public void importTalks(MorningEvent event) {
        List<Talk> sheetTalks = googleSheetsService.getTalks(eventRepository.findAll());

        List<Talk> sheetTalksForGivenEvent = sheetTalks.stream()
                .filter(t -> t.getEvent().getEventNumber() == event.getEventNumber())
                .collect(toList());

        for (Talk sheetTalk : sheetTalksForGivenEvent) {
            talkRepository.findByEventAndGoogleSheetsTimestamp(event, sheetTalk.getGoogleSheetsTimestamp())
                    .ifPresent(persistentTalk -> sheetTalk.setId(persistentTalk.getId()));
        }

        talkRepository.save(sheetTalksForGivenEvent);
    }

    public List<Talk> getTalks(MorningEvent morningEvent) {
        if (morningEvent.getId() != null) {
            return talkRepository.findByEvent(morningEvent);
        }
        return Collections.emptyList();
    }

    public Talk getTalk(long id) {
        return talkRepository.findOne(id);
    }

    public void save(Talk talk) {
        talkRepository.save(talk);
    }
}
