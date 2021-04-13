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

public class EveryYearPeriodTest {

    @Test
    public void serialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule());
        mapper.registerModule(new JavaTimeModule());

        EveryYearPeriod period = new EveryYearPeriod();
        period.setMonth(1);
        period.setDayOfMonth(2);
        period.setTime(LocalTime.of(3, 4));

        String json = mapper.writeValueAsString(period);
        Map<String, Object> result =
                mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        assertThat(result).containsOnlyKeys("every", "month", "dayOfMonth", "time");
        assertThat(result.get("every")).isEqualTo("YEAR");
        assertThat(result.get("month")).isEqualTo(1);
        assertThat(result.get("dayOfMonth")).isEqualTo(2);
        assertThat(result.get("time")).isEqualTo("03:04");
    }
}
