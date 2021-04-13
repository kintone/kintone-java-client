package com.kintone.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kintone.client.model.app.report.EveryMonthPeriod;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EveryMonthPeriodSerializerTest {

    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(EveryMonthPeriod.class, new EveryMonthPeriodSerializer());
        mapper = new ObjectMapper();
        mapper.registerModule(module);
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void serialize() throws IOException {
        EveryMonthPeriod period = new EveryMonthPeriod();
        period.setDayOfMonth(31);
        period.setTime(LocalTime.of(9, 30));

        String json = mapper.writeValueAsString(period);
        Map<String, Object> result =
                mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        assertThat(result).containsOnlyKeys("every", "dayOfMonth", "time");
        assertThat(result.get("every")).isEqualTo("MONTH");
        assertThat(result.get("dayOfMonth")).isEqualTo("31");
        assertThat(result.get("time")).isEqualTo("09:30");
    }
}
