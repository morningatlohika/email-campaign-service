package com.lohika.morning.ecs.domain.event;

import com.lohika.morning.ecs.BaseControllerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EventControllerTest extends BaseControllerTest {

    @Test
    public void testGetEvents_emptyDatabase() throws Exception {
        mockMvc.perform(get("/api/events")
                .header("Authorization", "Basic dXNlcjpxd2Vhc2Q="))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(HAL_JSON_CHARSET_UTF_8))
                .andExpect(content().string("{}"));
    }

    @Test
    public void testGetEvents_found() throws Exception {
        Event event = given.event("Golang").save();
        Event event2 = given.event("Robotic").save();

        // When
        ResultActions actions = mockMvc.perform(get("/api/events")
                .header("Authorization", "Basic dXNlcjpxd2Vhc2Q="))
                .andDo(print());

        // Then
        actions.andExpect(status().isOk())
                .andExpect(content().contentType(HAL_JSON_CHARSET_UTF_8));
                // TODO: match all events
                //.andExpect(eventsMatches(Arrays.asList(event, event2)));

        actions.andDo(document("{class-name}/{method-name}",
                responseFields(
                        fieldWithPath("_embedded").description("'event' array with event resources."),
                        fieldWithPath("_embedded.events").description("Array with returned Event resources."),
                        fieldWithPath("_embedded.events[].name").description("Event name."),
                        fieldWithPath("_embedded.events[].description").description("Event description."),
                        fieldWithPath("_embedded.events[].date").description("Event date."),
                        fieldWithPath("_embedded.events[]._links").description("Links."),
                        fieldWithPath("_embedded.events[]._links.self.href").description("Link to self section."))));
    }
//
//    /**
//     * Call "/api/events/{name}" no entities in DB. Expected NOT_FOUND(404, "Not Found").
//     */
//    @Test
//    public void testGetEvent_notFound() throws Exception {
//        // Given
//        Event event = given.event("Golang").save();
//        long nonexistentEventId = event.getId() + 1;
//
//        // When
//        ResultActions action = mockMvc.perform(get("/api/events/{id}", nonexistentEventId))
//                .andDo(print());
//
//        // Then
//        action.andExpect(status().isNotFound())
//                .andExpect(content().contentType(HAL_JSON_CHARSET_UTF_8))
//                .andExpect(jsonPath("$.message",
//                        is(format(MESSAGE_PATTERN_SINGULAR, Event.class.getSimpleName(), nonexistentEventId))));
//
//        action.andDo(document("{class-name}/{method-name}", errorMessageResponseFieldSnippet));
//    }
//
//    @Test
//    public void testGetEvent_found() throws Exception {
//        // Given
//        given.event(TEST_EVENT_C38950).save();
//        Event event = given.event(TEST_EVENT_SC1).save();
//
//        // When
//        ResultActions actions = mockMvc.perform(get("/api/events/{id}", event.getId()))
//                .andDo(print());
//
//        // Then
//        actions.andExpect(status().isOk())
//                .andExpect(eventMatches(event));
//
//        actions.andDo(document("{class-name}/{method-name}",
//                responseFields(
//                        fieldWithPath("name").description("Name of the Event entity."),
//                        fieldWithPath("description").description("Event description."),
//                        fieldWithPath("execute").description("Allows to enable or disable test event."),
//                        fieldWithPath("_links").description("Links section with self http reference."),
//                        fieldWithPath("_links.self.href").description("Link to self section."),
//                        fieldWithPath("_links.duplicate.href").description("Link to the \"Duplicate test event\" action.")),
//                links(halLinks(),
//                        linkWithRel("self").description("Link to self section."),
//                        linkWithRel("duplicate").description("Link to the \"Duplicate test event\" action."))));
//    }
//
//    @Test
//    public void testCreateEvent_created() throws Exception {
//        // Given
//        EventDto requestDto = eventDto(TEST_EVENT_SC1);
//        String requestBody = given.asJsonString(requestDto);
//
//        // When
//        ResultActions action = mockMvc.perform(post("/api/events")
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        assertEquals(1, eventRepository.count());
//        Event savedEvent = eventRepository.findByName(TEST_EVENT_SC1).get();
//
//        assertThat(requestDto, equalTo(savedEvent));
//
//        action.andExpect(status().isCreated())
//                .andExpect(header().string("Location", endsWith("/api/events/" + savedEvent.getId())));
//
//        action.andDo(document("{class-name}/{method-name}",
//                responseHeaders(
//                        headerWithName("Location").description(
//                                "URI path to created resource."))));
//    }
//
//    @Test
//    public void testCreateEvent_duplicateName() throws Exception {
//        // Given
//        given.event(TEST_EVENT_SC1).save();
//        String requestBody = given.asJsonString(eventDto(TEST_EVENT_SC1));
//
//        // When
//        ResultActions action = mockMvc.perform(post("/api/events")
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        assertEquals(1, eventRepository.count());
//
//        action.andExpect(status().isConflict())
//                .andExpect(jsonPath("$.message", is("Duplicate entry 'SC1' for key 'UK_name'")));
//
//        action.andDo(document("{class-name}/{method-name}", errorMessageResponseFieldSnippet));
//    }
//
//    @Test
//    public void testCreateEvent_missingName() throws Exception {
//        // Given
//        String requestBody = given.asJsonString(eventDto(null));
//
//        // When
//        ResultActions action = mockMvc.perform(post("/api/events")
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        assertEquals(0, eventRepository.count());
//
//        action.andExpect(status().isUnprocessableEntity())
//                .andExpect(validationErrorsMatches(new ValidationError("name", "may not be empty", "null")));
//
//        action.andDo(document("{class-name}/{method-name}", validationErrorResponseFieldSnippet));
//    }
//
//    @Test
//    public void testCreateEvent_blankName() throws Exception {
//        // Given
//        String requestBody = given.asJsonString(eventDto(BLANK_STRING));
//
//        // When
//        ResultActions action = mockMvc.perform(post("/api/events")
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        assertEquals(0, eventRepository.count());
//
//        action.andExpect(status().isUnprocessableEntity())
//                .andExpect(validationErrorsMatches(new ValidationError("name", "may not be empty", BLANK_STRING)));
//
//        action.andDo(document("{class-name}/{method-name}", validationErrorResponseFieldSnippet));
//    }
//
//    @Test
//    public void testUpdateEvent_updated() throws Exception {
//        // Given
//        TestStep testStep = given.testStep(TEST_STEP_ADDONS_MEAL).save();
//        Event event = given.event(TEST_EVENT_SC1).withTestSteps(testStep).save();
//        EventDto requestDto = EventDto.builder()
//                .name(TEST_EVENT_SC12)
//                .description("Some description")
//                .execute(true)
//                .build();
//
//        String requestBody = given.asJsonString(requestDto);
//
//        // When
//        ResultActions action = mockMvc.perform(put("/api/events/{id}", event.getId())
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        assertEquals(1, eventRepository.count());
//        Event updatedEvent = eventRepository.findById(event.getId()).get();
//
//        assertEquals(1, updatedEvent.getTestSteps().size());
//        assertEquals(testStep.getName(), updatedEvent.getTestSteps().get(0).getName());
//
//        assertThat(requestDto, equalTo(updatedEvent));
//
//        action.andExpect(status().isOk())
//                .andExpect(eventMatches(updatedEvent));
//
//        action.andDo(document("{class-name}/{method-name}",
//                responseFields(
//                        fieldWithPath("name").description("Name of the Event entity."),
//                        fieldWithPath("description").description("Event description."),
//                        fieldWithPath("execute").description("Allows to enable or disable test event."),
//                        fieldWithPath("_links").description("Links section with self http reference."),
//                        fieldWithPath("_links.duplicate.href").description("Link to the \"Duplicate test event\" action.")),
//                links(halLinks(),
//                        linkWithRel("self").description("Link to self section."),
//                        linkWithRel("duplicate").description("Link to the \"Duplicate test event\" action."))));
//    }
//
//    @Test
//    public void testUpdateEvent_emptyJson() throws Exception {
//        // Given
//        Event event = given.event(TEST_EVENT_SC1)
//                .withDescription("Some description")
//                .executed(true)
//                .save();
//
//        // When
//        ResultActions action = mockMvc.perform(put("/api/events/{id}", event.getId())
//                .content("{ }"))
//                .andDo(print());
//
//        // Then
//        assertEquals(1, eventRepository.count());
//        assertEquals(TEST_EVENT_SC1, eventRepository.findById(event.getId()).get().getName());
//
//        action.andExpect(status().isUnprocessableEntity())
//                .andExpect(validationErrorsMatches(
//                        new ValidationError("name", "may not be empty", "null"),
//                        new ValidationError("execute", "may not be null", "null")));
//
//        action.andDo(document("{class-name}/{method-name}", validationErrorResponseFieldSnippet));
//    }
//
//    @Test
//    public void testUpdateEvent_clearDescriptionExplicitly() throws Exception {
//        // Given
//        Event event = given.event(TEST_EVENT_SC1).withDescription("Non-empty description").save();
//        EventDto requestDto = EventDto.builder()
//                .name(TEST_EVENT_SC1)
//                .description(EMPTY_STRING)
//                .execute(true)
//                .build();
//
//        String requestBody = given.asJsonString(requestDto);
//
//        // When
//        ResultActions action = mockMvc.perform(put("/api/events/{id}", event.getId())
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        assertEquals(1, eventRepository.count());
//
//        Event updatedEvent = eventRepository.findById(event.getId()).get();
//        assertEquals(TEST_EVENT_SC1, updatedEvent.getName());
//        assertNull(updatedEvent.getDescription());
//
//        action.andExpect(status().isOk())
//                .andExpect(eventMatches(updatedEvent));
//
//        action.andDo(document("{class-name}/{method-name}"));
//    }
//
//    /*
//     * TODO (https://airasiadev.visualstudio.com/lohika-ddt-service/_workitems/edit/10933):
//     * TODO: ideally it should be impossible to modify any data implicitly.
//     */
//    @Test
//    public void testUpdateEvent_clearDescriptionImplicitly() throws Exception {
//        // Given
//        Event event = given.event(TEST_EVENT_SC1).withDescription("Non-empty description").save();
//        EventDto requestDto = EventDto.builder()
//                .name(TEST_EVENT_SC1)
//                .execute(true)
//                .build();
//
//        String requestBody = given.asJsonString(requestDto);
//
//        // When
//        ResultActions action = mockMvc.perform(put("/api/events/{id}", event.getId())
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        assertEquals(1, eventRepository.count());
//
//        Event updatedEvent = eventRepository.findById(event.getId()).get();
//        assertEquals(TEST_EVENT_SC1, updatedEvent.getName());
//        assertNull(updatedEvent.getDescription());
//
//        action.andExpect(status().isOk())
//                .andExpect(eventMatches(updatedEvent));
//
//        action.andDo(document("{class-name}/{method-name}"));
//    }
//
//    @Test
//    public void testUpdateEvent_notFound() throws Exception {
//        // Given
//        Event event = given.event(TEST_EVENT_SC1).save();
//        long idToUpdate = event.getId() + 1;
//        String requestBody = given.asJsonString(eventDto(TEST_EVENT_SC12));
//
//        // When
//        ResultActions action = mockMvc.perform(put("/api/events/{id}", idToUpdate)
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        assertEquals(1, eventRepository.count());
//        assertEquals(TEST_EVENT_SC1, eventRepository.findById(event.getId()).get().getName());
//
//        action.andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message", is(format(MESSAGE_PATTERN_SINGULAR, Event.class.getSimpleName(),
//                        idToUpdate))));
//
//        action.andDo(document("{class-name}/{method-name}", errorMessageResponseFieldSnippet));
//    }
//
//    @Test
//    public void testUpdateEvent_duplicateName() throws Exception {
//        // Given
//        Event event = given.event(TEST_EVENT_SC1).save();
//        Event eventToUpdate = given.event(TEST_EVENT_SC12).save();
//
//        String requestBody = given.asJsonString(eventDto(TEST_EVENT_SC1));
//
//        // When
//        ResultActions action = mockMvc.perform(put("/api/events/{id}", eventToUpdate.getId())
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        assertEquals(2, eventRepository.count());
//
//        assertEquals(TEST_EVENT_SC1, eventRepository.findById(event.getId()).get().getName());
//        assertEquals(TEST_EVENT_SC12, eventRepository.findById(eventToUpdate.getId()).get().getName());
//
//        action.andExpect(status().isConflict())
//                .andExpect(jsonPath("$.message", is("Duplicate entry 'SC1' for key 'UK_name'")));
//
//        action.andDo(document("{class-name}/{method-name}", errorMessageResponseFieldSnippet));
//    }
//
//    @Test
//    public void testUpdateEvent_missingName() throws Exception {
//        // Given
//        Event event = given.event(TEST_EVENT_SC1).save();
//        String requestBody = given.asJsonString(eventDto(null));
//
//        // When
//        ResultActions action = mockMvc.perform(put("/api/events/{id}", event.getId())
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        assertEquals(1, eventRepository.count());
//        assertEquals(TEST_EVENT_SC1, eventRepository.findById(event.getId()).get().getName());
//
//        action.andExpect(status().isUnprocessableEntity())
//                .andExpect(validationErrorsMatches(new ValidationError("name", "may not be empty", "null")));
//
//        action.andDo(document("{class-name}/{method-name}", validationErrorResponseFieldSnippet));
//    }
//
//    @Test
//    public void testUpdateEvent_blankName() throws Exception {
//        // Given
//        Event event = given.event(TEST_EVENT_SC1).save();
//        String requestBody = given.asJsonString(eventDto(BLANK_STRING));
//
//        // When
//        ResultActions action = mockMvc.perform(put("/api/events/{id}", event.getId())
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        assertEquals(1, eventRepository.count());
//        assertEquals(TEST_EVENT_SC1, eventRepository.findById(event.getId()).get().getName());
//
//        action.andExpect(status().isUnprocessableEntity())
//                .andExpect(validationErrorsMatches(new ValidationError("name", "may not be empty", BLANK_STRING)));
//
//        action.andDo(document("{class-name}/{method-name}", validationErrorResponseFieldSnippet));
//    }
//
//    @Test
//    public void testDeleteEvent_deleted() throws Exception {
//        // Given
//        Event eventToDelete = given.event(TEST_EVENT_SC1).save();
//        Event eventToKeep = given.event(TEST_EVENT_SC12).save();
//
//        // When
//        ResultActions action = mockMvc.perform(delete("/api/events/{id}", eventToDelete.getId()))
//                .andDo(print());
//
//        // Then
//        assertEquals(1, eventRepository.count());
//        Event remainingEvent = eventRepository.findById(eventToKeep.getId()).get();
//        assertEquals(TEST_EVENT_SC12, remainingEvent.getName());
//
//        action.andExpect(status().isNoContent())
//                .andExpect(content().string(isEmptyString()));
//
//        action.andDo(document("{class-name}/{method-name}"));
//    }
//
//    @Test
//    public void testDeleteEvent_verifyValuesDeleted() throws Exception {
//        // Given
//        Keyword keyword = given.keyword(KEYWORD_WEBEDIT).save();
//        TestStepAttribute attribute = ATTRIBUTE_ORIGIN.get();
//        attribute.setKeyword(keyword);
//
//        TestStep testStep = given.testStep(TEST_STEP_ADDONS_MEAL).withAttributes(attribute).save();
//
//        Event eventToDelete = given.event(TEST_EVENT_SC1).withTestSteps(testStep).save();
//        Event eventToKeep = given.event(TEST_EVENT_SC12).withTestSteps(testStep).save();
//
//        AttributeValue valueToDelete = given.attributeValue("To Delete").event(eventToDelete).testStep(testStep).testStepAttribute(attribute).save();
//        AttributeValue valueToKeep = given.attributeValue("To Keep").event(eventToKeep).testStep(testStep).testStepAttribute(attribute).save();
//
//        // When
//        ResultActions action = mockMvc.perform(delete("/api/events/{id}", eventToDelete.getId()))
//                .andDo(print());
//
//        // Then
//        List<AttributeValue> remainingValues = attributeValueRepository.findAll();
//        assertEquals(1, remainingValues.size());
//
//        ReflectionMatchers reflection = ignoringFields("testStepAttribute");
//        assertThat(remainingValues, contains(reflection.equalToIgnoringFields(valueToKeep)));
//        assertThat(remainingValues, not(contains(reflection.equalToIgnoringFields(valueToDelete))));
//
//        action.andExpect(status().isNoContent())
//                .andExpect(content().string(isEmptyString()));
//    }
//
//    @Test
//    public void testDeleteEvent_notFound() throws Exception {
//        // Given
//        Event event = given.event(TEST_EVENT_SC1).save();
//        long idToDelete = event.getId() + 1;
//
//        // When
//        ResultActions action = mockMvc.perform(delete("/api/events/{id}", idToDelete))
//                .andDo(print());
//
//        // Then
//        assertEquals(1, eventRepository.count());
//        Event remainingEvent = eventRepository.findById(event.getId()).get();
//        assertEquals(TEST_EVENT_SC1, remainingEvent.getName());
//
//        action.andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message",
//                        is(format("Resource Event#{} does not exist.", idToDelete))));
//
//        action.andDo(document("{class-name}/{method-name}", errorMessageResponseFieldSnippet));
//    }
//
//    @Test
//    public void testUpdateTestSteps_addSteps() throws Exception {
//        // Given
//        TestStep testStep1 = given.testStep(TEST_STEP_ADDONS_MEAL).save();
//        TestStep testStep2 = given.testStep(TEST_STEP_SEARCH_FLIGHT).save();
//        Event event = given.event(TEST_EVENT_SC1).save();
//
//        given.testStep("NOISE_STEP1").save();
//        given.testStep("NOISE_STEP2").save();
//        given.event("NOISE_EVENT1").save();
//
//        String requestBody = given.asJsonString(new Long[]{testStep1.getId(), testStep2.getId()});
//
//        // When
//        ResultActions action = mockMvc.perform(put("/api/events/{id}/testSteps", event.getId())
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        assertEquals(2, eventRepository.count());
//        assertEquals(4, testStepRepository.count());
//        Event loadedEvent = eventRepository.findById(event.getId()).get();
//
//        action.andExpect(status().isNoContent());
//
//        List<Long> testStepIds2 = extractTestStepIds(loadedEvent);
//        assertThat(testStepIds2, IsIterableContainingInOrder.contains(testStep1.getId(), testStep2.getId()));
//
//        action.andDo(document("{class-name}/{method-name}"));
//    }
//
//    @Test
//    public void testUpdateTestSteps_duplicates() throws Exception {
//        // Given
//        Event event = given.event(TEST_EVENT_C38950).save();
//        TestStep mealStep = given.testStep(TEST_STEP_ADDONS_MEAL).save();
//        TestStep searchStep = given.testStep(TEST_STEP_SEARCH_FLIGHT).save();
//        long[] duplicatedIds = {mealStep.getId(), searchStep.getId(), mealStep.getId(), searchStep.getId()};
//        String requestBody = given.asJsonString(duplicatedIds);
//
//        // When
//        ResultActions action = mockMvc.perform(put("/api/events/{id}/testSteps", event.getId())
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        action.andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message", is(format(MESSAGE_PATTERN, duplicatedIds))));
//
//        action.andDo(document("{class-name}/{method-name}",
//                responseFields(fieldWithPath("message")
//                        .description("The message with IDs that were failed due to duplicate validation."))));
//    }
//
//    @Test
//    public void testUpdateTestSteps_stepsInvalidInput() throws Exception {
//        // Given
//        String requestBody = given.asJsonString(
//                new Object[]{1L, 2L, 3L, "INVALID_INPUT", 5L, 6L, 7L, 8L}
//        );
//
//        // When
//        ResultActions action = mockMvc.perform(put("/api/events/{id}/testSteps", 1L)
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        action.andExpect(status().isBadRequest());
//
//        action.andDo(document("{class-name}/{method-name}"));
//    }
//
//    @Test
//    public void testUpdateTestSteps_eventInvalidInput() throws Exception {
//        // Given
//        String requestBody = given.asJsonString(
//                new Object[]{1L, 2L, 3L}
//        );
//
//        // When
//        ResultActions action = mockMvc.perform(put("/api/events/{id}/testSteps", "INVALID_INPUT")
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        action.andExpect(status().isBadRequest());
//
//        action.andDo(document("{class-name}/{method-name}"));
//    }
//
//    @Test
//    public void testUpdateTestSteps_stepsNotExists() throws Exception {
//        // Given
//        TestStep testStep1 = given.testStep(TEST_STEP_SEARCH_FLIGHT).save();
//        TestStep testStep2 = given.testStep(TEST_STEP_ADDONS_MEAL).save();
//
//        Event event = given.event(TEST_EVENT_C38950).save();
//        final Long nonExistingTestStepId1 = testStep2.getId() + 1;
//        final Long nonExistingTestStepId2 = testStep2.getId() + 2;
//
//        String requestBody = given.asJsonString(
//                new Long[]{testStep1.getId(), nonExistingTestStepId1, testStep2.getId(), nonExistingTestStepId2}
//        );
//
//        // When
//        ResultActions action = mockMvc.perform(put("/api/events/{id}/testSteps", event.getId())
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        action.andExpect(status().isNotFound())
//                .andExpect(content().contentType(HAL_JSON_CHARSET_UTF_8))
//                .andExpect(jsonPath("$.message",
//                        is(format(MESSAGE_PATTERN_PLURAL, TestStep.class.getSimpleName(),
//                                new long[]{nonExistingTestStepId1, nonExistingTestStepId2}))));
//
//        action.andDo(document("{class-name}/{method-name}",
//                responseFields(fieldWithPath("message")
//                        .description("Detailed error message with IDs that were not found in DB."))));
//    }
//
//    @Test
//    public void testUpdateTestSteps_eventNotExists() throws Exception {
//        // Given
//        TestStep testStep1 = given.testStep(TEST_STEP_SEARCH_FLIGHT).save();
//        TestStep testStep2 = given.testStep(TEST_STEP_ADDONS_MEAL).save();
//
//        Event event = given.event(TEST_EVENT_C38950).save();
//        final long nonExistingEventId = event.getId() + 1;
//
//        String requestBody = given.asJsonString(
//                new Long[]{testStep1.getId(), testStep2.getId()}
//        );
//
//        // When
//        ResultActions action = mockMvc.perform(put("/api/events/{id}/testSteps", nonExistingEventId)
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        action.andExpect(status().isNotFound())
//                .andExpect(content().contentType(HAL_JSON_CHARSET_UTF_8))
//                .andExpect(jsonPath("$.message",
//                        is(format(MESSAGE_PATTERN_SINGULAR, Event.class.getSimpleName(), nonExistingEventId))));
//
//        action.andDo(document("{class-name}/{method-name}", responseFields(
//                fieldWithPath("message").description("Detailed error message."))));
//    }
//
//    @Test
//    public void testUpdateTestSteps_reorder() throws Exception {
//        // Given
//        TestStep testStep1 = given.testStep("STEP1").save();
//        TestStep testStep2 = given.testStep("STEP2").save();
//        TestStep testStep3 = given.testStep("STEP3").save();
//        TestStep testStep4 = given.testStep("STEP4").save();
//
//        Event event = given.event(TEST_EVENT_C38950)
//                .withTestSteps(testStep1, testStep2, testStep3, testStep4).save();
//
//        String requestBody = given.asJsonString(
//                new Long[]{testStep1.getId(), testStep3.getId(), testStep4.getId(), testStep2.getId()}
//        );
//
//        // When
//        assertEquals(4, testStepRepository.count());
//
//        ResultActions action = mockMvc.perform(put("/api/events/{id}/testSteps", event.getId())
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        action.andExpect(status().isNoContent());
//        Event loadedEvent = eventRepository.findById(event.getId()).get();
//
//        List<Long> testStepIds = extractTestStepIds(loadedEvent);
//        assertThat(testStepIds, IsIterableContainingInOrder.contains(
//                testStep1.getId(), testStep3.getId(), testStep4.getId(), testStep2.getId()));
//
//        action.andDo(document("{class-name}/{method-name}"));
//    }
//
//    @Test
//    public void testUpdateTestSteps_delete() throws Exception {
//        // Given
//        TestStep testStep1 = given.testStep("STEP1").save();
//        TestStep testStep2 = given.testStep("STEP2").save();
//        TestStep testStepToDelete1 = given.testStep("STEP_DEL1").save();
//        TestStep testStep3 = given.testStep("STEP3").save();
//        TestStep testStep4 = given.testStep("STEP4").save();
//        TestStep testStepToDelete2 = given.testStep("STEP4_DEL2").save();
//
//        Event event = given.event(TEST_EVENT_C38950)
//                .withTestSteps(testStep1, testStep2, testStepToDelete1, testStep3, testStep4, testStepToDelete2)
//                .save();
//
//        String requestBody = given.asJsonString(
//                new Long[]{testStep1.getId(), testStep2.getId(), testStep3.getId(), testStep4.getId()}
//        );
//
//        // When
//        ResultActions action = mockMvc.perform(put("/api/events/{id}/testSteps", event.getId())
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        action.andExpect(status().isNoContent());
//        Event loadedEvent = eventRepository.findById(event.getId()).get();
//
//        List<Long> testStepIds = extractTestStepIds(loadedEvent);
//        assertThat(testStepIds, IsIterableContainingInOrder.contains(
//                testStep1.getId(), testStep2.getId(), testStep3.getId(), testStep4.getId()));
//
//        assertEquals(6, testStepRepository.count());
//
//        action.andDo(document("{class-name}/{method-name}"));
//    }
//
//    @Test
//    public void testUpdateTestSteps_deleteVerifyValuesDeleted() throws Exception {
//        // Given
//        Keyword keyword2 = given.keyword(KEYWORD_WEBEDIT).save();
//        TestStepAttribute attribute11 = given.attribute(ATTRIBUTE_ORIGIN.get()).withKeyword(keyword2).build();
//        TestStepAttribute attribute12 = given.attribute(ATTRIBUTE_DESTINATION.get()).withKeyword(keyword2).build();
//        TestStep testStepToKeep = given.testStep("STEP1").withAttributes(attribute11, attribute12).save();
//
//        Keyword keyword1 = given.keyword(KEYWORD_WEBBUTTON).save();
//        TestStepAttribute attribute21 = given.attribute(ATTRIBUTE_ONE_WAY.get()).withKeyword(keyword1).build();
//        TestStepAttribute attribute22 = given.attribute(ATTRIBUTE_ROUND_TRIP.get()).withKeyword(keyword1).build();
//        TestStep testStepToDelete = given.testStep("STEP_DEL1").withAttributes(attribute21, attribute22).save();
//
//        Event event = given.event(TEST_EVENT_SC1)
//                .withTestSteps(testStepToKeep, testStepToDelete)
//                .save();
//
//        AttributeValue value11 = given.attributeValue("1_1").event(event).testStep(testStepToKeep).testStepAttribute(attribute11).save();
//        AttributeValue value12 = given.attributeValue("1_2").event(event).testStep(testStepToKeep).testStepAttribute(attribute12).save();
//        given.attributeValue("2_1").event(event).testStep(testStepToDelete).testStepAttribute(attribute21).save();
//        given.attributeValue("2_2").event(event).testStep(testStepToDelete).testStepAttribute(attribute22).save();
//
//        Event noiseEvent = given.event(TEST_EVENT_C38950).withTestSteps(testStepToKeep).save();
//        AttributeValue noiseValue = given.attributeValue("noise").event(noiseEvent).testStep(testStepToDelete).testStepAttribute(attribute22).save();
//
//        String requestBody = given.asJsonString(new Long[]{testStepToKeep.getId()});
//
//        // When
//        ResultActions action = mockMvc.perform(put("/api/events/{id}/testSteps", event.getId())
//                .content(requestBody))
//                .andDo(print());
//
//        // Then
//        action.andExpect(status().isNoContent());
//
//        Event loadedEvent = eventRepository.findById(event.getId()).get();
//        assertEquals(1, loadedEvent.getTestSteps().size());
//        List<Long> testStepIds = extractTestStepIds(loadedEvent);
//        assertThat(testStepIds, IsIterableContainingInOrder.contains(testStepToKeep.getId()));
//
//        assertEquals(2, testStepRepository.count());
//        assertEquals(3, attributeValueRepository.count());
//
//        ReflectionMatchers reflection = ignoringFields("testStepAttribute");
//        assertThat(attributeValueRepository.findAll(), containsInAnyOrder(
//                reflection.equalToIgnoringFields(value11),
//                reflection.equalToIgnoringFields(value12),
//                reflection.equalToIgnoringFields(noiseValue)));
//    }
//
//    @Test
//    public void testGetTestSteps() throws Exception {
//        // Given
//        given.event(TEST_EVENT_C38950).withTestSteps(1).save();
//
//        TestStep testStep2 = given.testStep(TEST_STEP_ADDONS_MEAL).save();
//        TestStep testStep3 = given.testStep(TEST_STEP_GUEST_DETAILS).save();
//        Event event = given.event(TEST_EVENT_SC1).withTestSteps(testStep2, testStep3).save();
//
//
//        // When
//        ResultActions action = mockMvc.perform(get("/api/events/{id}/testSteps", event.getId()))
//                .andDo(print());
//
//        // Then
//        action.andExpect(status().isOk())
//                .andExpect(eventStepsMatches(event, Arrays.asList(testStep2, testStep3)));
//
//        action.andDo(document("{class-name}/{method-name}",
//                responseFields(
//                        fieldWithPath("_embedded.testSteps").description("Collection of test steps assigned to the test event"),
//                        fieldWithPath("_embedded.testSteps[].name").description("Test step name."),
//                        fieldWithPath("_embedded.testSteps[]._links").description("Links section."),
//                        fieldWithPath("_embedded.testSteps[]._links.attributes.href").description("Link to the test step attributes with values."))));
//    }
//
//    @Test
//    public void testGetTestSteps_noTestSteps() throws Exception {
//        // Given
//        given.event(TEST_EVENT_C38950).withTestSteps(1).save();
//        Event event = given.event(TEST_EVENT_SC1).save();
//
//        // When
//        ResultActions action = mockMvc.perform(get("/api/events/{id}/testSteps", event.getId()))
//                .andDo(print());
//
//        // Then
//        action.andExpect(status().isOk())
//                .andExpect(content().string("{ }"));
//
//        action.andDo(document("{class-name}/{method-name}"));
//    }
//
//    @Test
//    public void testGetTestSteps_invalidEventId() throws Exception {
//        // Given
//        given.event(TEST_EVENT_C38950).withTestSteps(1).save();
//        Event event = given.event(TEST_EVENT_SC1).save();
//        long nonExistingEventId = event.getId() + 1;
//
//        // When
//        ResultActions action = mockMvc.perform(get("/api/events/{id}/testSteps", nonExistingEventId))
//                .andDo(print());
//
//        // Then
//        action.andExpect(status().isNotFound())
//                .andExpect(content().contentType(HAL_JSON_CHARSET_UTF_8))
//                .andExpect(jsonPath("$.message",
//                        is(format(MESSAGE_PATTERN_SINGULAR, Event.class.getSimpleName(), nonExistingEventId))));
//
//        action.andDo(document("{class-name}/{method-name}", responseFields(
//                fieldWithPath("message").description("Detailed error message."))));
//    }
//
//
//    @Test
//    public void testDuplicateEvent_created() throws Exception {
//        // Given
//        TestStepAttribute attribute1 = given.attribute(ATTRIBUTE_ONE_WAY.get())
//                .withKeyword(given.keyword(KEYWORD_WEBBUTTON).save())
//                .build();
//
//        TestStepAttribute attribute2 = given.attribute(ATTRIBUTE_ORIGIN.get())
//                .withKeyword(given.keyword(KEYWORD_WEBEDIT).save())
//                .build();
//
//        TestStep testStep1 = given.testStep(TEST_STEP_GUEST_DETAILS).withAttributes(attribute1).save();
//        TestStep testStep2 = given.testStep(TEST_STEP_ADDONS_MEAL).withAttributes(attribute2).save();
//
//        Event event = given.event(TEST_EVENT_SC1).withTestSteps(testStep1, testStep2).save();
//
//        AttributeValue value1 = given.attributeValue("1").event(event).testStep(testStep1).testStepAttribute(attribute1).save();
//        AttributeValue value2 = given.attributeValue("2").event(event).testStep(testStep2).testStepAttribute(attribute2).save();
//
//        // When
//        ResultActions action = mockMvc.perform(post("/api/events/{id}/duplicate", event.getId()))
//                .andDo(print());
//
//        // Then
//        assertEquals(2, testStepRepository.count());
//        assertEquals(2, testStepAttributeRepository.count());
//        assertEquals(2, eventRepository.count());
//        assertEquals(4, attributeValueRepository.count());
//
//        Event copiedEvent = eventRepository.findByName(event.getName() + " - copy 1").get();
//
//        List<TestStep> copiedSteps = copiedEvent.getTestSteps();
//        assertEquals(2, copiedSteps.size());
//
//        ReflectionMatchers stepReflectionMatcher = ignoringFields("testStepAttributes");
//        assertThat(copiedSteps, containsInAnyOrder(
//                stepReflectionMatcher.equalToIgnoringFields(testStep1),
//                stepReflectionMatcher.equalToIgnoringFields(testStep2)));
//
//        List<AttributeValue> copiedValues = attributeValueRepository.findByEventId(copiedEvent.getId());
//
//        ReflectionMatchers valueReflectionMatcher = ignoringFields("id", "eventId", "testStepAttribute");
//        assertThat(copiedValues, containsInAnyOrder(
//                valueReflectionMatcher.equalToIgnoringFields(value1),
//                valueReflectionMatcher.equalToIgnoringFields(value2)));
//
//        action.andExpect(status().isCreated())
//                .andExpect(header().string("Location", endsWith("/api/events/" + copiedEvent.getId())));
//
//        action.andDo(document("{class-name}/{method-name}",
//                responseHeaders(headerWithName("Location").description("URI path to created resource."))));
//    }
//
//    @Test
//    public void testDuplicateEvent_notFound() throws Exception {
//        // Given
//        Event event = given.event(TEST_EVENT_SC1).save();
//        long nonexistingEventId = event.getId() + 1;
//
//        // When
//        ResultActions action = mockMvc.perform(post("/api/events/{id}/duplicate", nonexistingEventId))
//                .andDo(print());
//
//        // Then
//        assertEquals(1, eventRepository.count());
//
//        action.andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message",
//                        is(format("Resource Event#{} does not exist.", nonexistingEventId))));
//
//        action.andDo(document("{class-name}/{method-name}", errorMessageResponseFieldSnippet));
//    }
//
//    @Test
//    public void testDuplicateTestStep_oneMoreCopy() throws Exception {
//        // Given
//        Event event = given.event(TEST_EVENT_SC1).save();
//        given.event(TEST_EVENT_SC1 + " - copy 4").save();
//
//        // When
//        ResultActions action = mockMvc.perform(post("/api/events/{id}/duplicate", event.getId()))
//                .andDo(print());
//
//        // Then
//        assertEquals(3, eventRepository.count());
//
//        Event copiedEvent = eventRepository.findByName(event.getName() + " - copy 5").get();
//
//        action.andExpect(status().isCreated())
//                .andExpect(header().string("Location", endsWith("/api/events/" + copiedEvent.getId())));
//    }
//
//    private List<Long> extractTestStepIds(Event loadedEvent) {
//        return loadedEvent.getTestSteps().stream()
//                .map(TestStep::getId).collect(Collectors.toList());
//    }

}

//class {
//    @Test
//    public void createEvent() throws Exception {
//    }
//
//    @Test
//    public void getEvent() throws Exception {
//    }
//
//}