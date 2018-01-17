package com.lohika.morning.ecs;

import com.lohika.morning.ecs.domain.event.EventRepository;
import com.lohika.morning.ecs.domain.talk.TalkRepository;
import com.lohika.morning.ecs.util.TestDataGenerator;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseTest {

    @Autowired
    protected TestDataGenerator given;

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