package com.kintone.client.model.app.report;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalTime;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class EveryDayPeriodTest {

    @Test
    public void serialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule());
        mapper.registerModule(new JavaTimeModule());

        EveryDayPeriod period = new EveryDayPeriod();
        period.setTime(LocalTime.of(0, 0));

        String json = mapper.writeValueAsString(period);
        Map<String, Object> result =
                mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        assertThat(result).containsOnlyKeys("every", "time");
        assertThat(result.get("every")).isEqualTo("DAY");
        assertThat(result.get("time")).isEqualTo("00:00");
    }
}
