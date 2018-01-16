package com.lohika.morning.ecs;

import com.lohika.morning.ecs.domain.event.EventRepository;
import com.lohika.morning.ecs.domain.talk.TalkRepository;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseCleanupTest {

    @Autowired
    protected EventRepository eventRepository;

    @Autowired
    protected TalkRepository talkRepository;

    @After
    public void cleanUp() {
        talkRepository.deleteAll();
        eventRepository.deleteAll();
    }
}