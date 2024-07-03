package com.kintone.client.model.app.field;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kintone.client.model.record.FieldType;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TimeFieldPropertyTest {
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void deserialize() throws IOException {
        URL url = getClass().getResource("TimeFieldPropertyTest_deserialize.json");

        TimeFieldProperty result = mapper.readValue(url, TimeFieldProperty.class);
        assertThat(result.getType()).isEqualTo(FieldType.TIME);
        assertThat(result.getCode()).isEqualTo("time");
        assertThat(result.getLabel()).isEqualTo("Time field");
        assertThat(result.getNoLabel()).isTrue();
        assertThat(result.getRequired()).isTrue();
        assertThat(result.getDefaultValue()).isEqualTo(LocalTime.of(5, 0));
        assertThat(result.getDefaultNowValue()).isFalse();
    }

    @Test
    public void deserialize_defaultValueEmpty() throws IOException {
        URL url = getClass().getResource("TimeFieldPropertyTest_deserialize_defaultValueEmpty.json");

        TimeFieldProperty result = mapper.readValue(url, TimeFieldProperty.class);
        assertThat(result.getType()).isEqualTo(FieldType.TIME);
        assertThat(result.getCode()).isEqualTo("time");
        assertThat(result.getLabel()).isEqualTo("Time field");
        assertThat(result.getNoLabel()).isFalse();
        assertThat(result.getRequired()).isFalse();
        assertThat(result.getDefaultValue()).isNull();
        assertThat(result.getDefaultNowValue()).isTrue();
    }
}
