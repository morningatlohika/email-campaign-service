package com.lohika.morning.ecs.service;

import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.lohika.morning.ecs.domain.talk.Talk;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class GoogleSheetsServiceTest {

    @Autowired
    private GoogleSheetsService googleSheetsService;

    @Test
    public void test2() throws IOException {
        // Given
        MorningEvent event1 = MorningEvent.builder().id(1L).build();
        MorningEvent event2 = MorningEvent.builder().id(2L).build();
        MorningEvent event3 = MorningEvent.builder().id(3L).build();

        // When
        List<Talk> talks = googleSheetsService.getTalks(Arrays.asList(event1, event2, event3));

        // Then
        // Talk #1
        Talk talk = talks.get(0);
        assertEquals("Red color in nature", talk.getTitle());

        assertEquals(2, talk.getSpeakers().size());
        assertEquals("Red", talk.getSpeakers().get(0).getLastName());
        assertEquals("Green", talk.getSpeakers().get(1).getLastName());
        assertEquals(1L, talk.getEvent().getId().longValue());

        // Talk #2
        talk = talks.get(1);
        assertEquals("Why is the water Blue?", talk.getTitle());

        assertEquals(1, talk.getSpeakers().size());
        assertEquals("Blue", talk.getSpeakers().get(0).getLastName());
        assertEquals(2L, talk.getEvent().getId().longValue());

        // Talk #3
        talk = talks.get(2);
        assertEquals("Magenta wash machines", talk.getTitle());

        assertEquals(1, talk.getSpeakers().size());
        assertEquals("Magenta", talk.getSpeakers().get(0).getLastName());
        assertNull(talk.getEvent());
    }

}