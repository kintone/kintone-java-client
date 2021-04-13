package com.kintone.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kintone.client.model.app.report.EveryQuarterPeriod;
import com.kintone.client.model.app.report.QuarterlyPattern;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EveryQuarterPeriodSerializerTest {

    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(EveryQuarterPeriod.class, new EveryQuarterPeriodSerializer());
        mapper = new ObjectMapper();
        mapper.registerModule(module);
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void serialize() throws IOException {
        EveryQuarterPeriod period = new EveryQuarterPeriod();
        period.setPattern(QuarterlyPattern.JAN_APR_JUL_OCT);
        period.setEndOfMonth();
        period.setTime(LocalTime.of(23, 59));

        String json = mapper.writeValueAsString(period);
        Map<String, Object> result =
                mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        assertThat(result).containsOnlyKeys("every", "pattern", "dayOfMonth", "time");
        assertThat(result.get("every")).isEqualTo("QUARTER");
        assertThat(result.get("pattern")).isEqualTo("JAN_APR_JUL_OCT");
        assertThat(result.get("dayOfMonth")).isEqualTo("END_OF_MONTH");
        assertThat(result.get("time")).isEqualTo("23:59");
    }
}
