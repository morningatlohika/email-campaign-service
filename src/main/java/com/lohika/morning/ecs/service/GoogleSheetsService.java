package com.lohika.morning.ecs.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.lohika.morning.ecs.domain.event.MorningEvent;
import com.lohika.morning.ecs.domain.speaker.Speaker;
import com.lohika.morning.ecs.domain.talk.Talk;
import com.lohika.morning.ecs.utils.EcsUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@Slf4j
public class GoogleSheetsService {

  @Autowired(required = false)
  private Sheets sheetsClient;

  @Value("${google.speaker-application-form.spreadsheet-id}")
  private String sheetId;

  @Value("${google.speaker-application-form.sheet-title}")
  private String sheetTitle;

  @Value("${google.speaker-application-form.range}")
  private String range;

  @SneakyThrows
  public List<Talk> getTalks(List<MorningEvent> events) {
    return new TalkMapper(events).map(readValues(EcsUtils.formatString("{}!{}", sheetTitle, range)));
  }

  private List<List<String>> readValues(String range) throws IOException {
    log.info("Reading data from https://docs.google.com/spreadsheets/d/{}/edit", sheetId);
    ValueRange response = sheetsClient.spreadsheets().values()
        .get(sheetId, range)
        .execute();

    List values = response.getValues();

    if (CollectionUtils.isEmpty(values)) {
      log.debug("No data found in the sheet");
    }
    return values;
  }

  private static class SpeakerMapper {

    private List<Speaker> mapRow(List<String> row) {
      List<Speaker> speakers = new ArrayList<>();
      speakers.add(parseSpeaker(row, 2));

      if (BooleanUtils.toBooleanObject(row.get(17))) {
        speakers.add(parseSpeaker(row, 18));
      }

      return speakers;
    }

    private Speaker parseSpeaker(List<String> row, int startColumn) {
      return Speaker.builder()
          .firstName(row.get(startColumn))
          .lastName(row.get(++startColumn))
          .company(row.get(++startColumn))
          .position(row.get(++startColumn))
          .about(row.get(++startColumn))
          .webProfileUrl(row.get(++startColumn))
          .photoUrl(row.get(++startColumn))
          .email(row.get(++startColumn))
          .city(row.get(++startColumn))
          .hotelOption(row.get(++startColumn))
          .transportTicketOption(row.get(++startColumn))
          .additionalTravelInfo(row.get(++startColumn))
          // This is the same for both presenters:
          .equipmentOption(row.get(32))
          .build();
    }
  }

  private static class TalkMapper {
    private final SpeakerMapper speakerMapper;

    /**
     * Key: string representation of the MorningEvent id
     * Value: morning event itself
     */
    private final Map<String, MorningEvent> events;

    private TalkMapper(List<MorningEvent> events) {
      this.events = events.stream().collect(toMap(e -> Integer.toString(e.getEventNumber()), identity()));
      this.speakerMapper = new SpeakerMapper();
    }

    public Talk mapRow(List<String> row) {
      return Talk.builder()
          .googleSheetsTimestamp(row.get(1))
          .title(row.get(14))
          .theses(textToHtml(row.get(15)))
          .language(Talk.Language.fromString(row.get(16).toUpperCase()))
          .speakers(speakerMapper.mapRow(row))
          .event(events.get(row.get(0)))
          .level(Talk.Level.fromString(row.get(33)))
          .build();
    }

    public List<Talk> map(List<List<String>> rows) {
      return rows.stream().map(this::mapRow)
          .filter(t -> t.getEvent() != null)
          .collect(toList());
    }

    private String textToHtml(String text) {
      return text.replaceAll("\n", "<br/>");
    }
  }
}
