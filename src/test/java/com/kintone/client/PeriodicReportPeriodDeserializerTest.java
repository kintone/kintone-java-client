package com.kintone.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kintone.client.model.app.report.*;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PeriodicReportPeriodDeserializerTest {
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(PeriodicReportPeriod.class, new PeriodicReportPeriodDeserializer());
        mapper = new ObjectMapper();
        mapper.registerModule(module);
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void deserialize_YEAR() throws IOException {
        URL url = getClass().getResource("PeriodicReportPeriodDeserializerTest_deserialize_YEAR.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(EveryYearPeriod.class);

        EveryYearPeriod obj = (EveryYearPeriod) result.getProperty();
        assertThat(obj.getEvery()).isEqualTo(IntervalType.YEAR);
        assertThat(obj.getMonth()).isEqualTo(12);
        assertThat(obj.getDayOfMonth()).isEqualTo(31);
        assertThat(obj.getTime()).isEqualTo(LocalTime.of(23, 59));
    }

    @Test
    public void deserialize_QUARTER() throws IOException {
        URL url =
                getClass().getResource("PeriodicReportPeriodDeserializerTest_deserialize_QUARTER.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(EveryQuarterPeriod.class);

        EveryQuarterPeriod obj = (EveryQuarterPeriod) result.getProperty();
        assertThat(obj.getEvery()).isEqualTo(IntervalType.QUARTER);
        assertThat(obj.getPattern()).isEqualTo(QuarterlyPattern.MAR_JUN_SEP_DEC);
        assertThat(obj.getDayOfMonth()).isEqualTo(1);
        assertThat(obj.getTime()).isEqualTo(LocalTime.of(00, 00));
    }

    @Test
    public void deserialize_MONTH() throws IOException {
        URL url = getClass().getResource("PeriodicReportPeriodDeserializerTest_deserialize_MONTH.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(EveryMonthPeriod.class);

        EveryMonthPeriod obj = (EveryMonthPeriod) result.getProperty();
        assertThat(obj.getEvery()).isEqualTo(IntervalType.MONTH);
        assertThat(obj.isEndOfMonth()).isTrue();
        assertThat(obj.getDayOfMonth()).isEqualTo(null);
        assertThat(obj.getTime()).isEqualTo(LocalTime.of(12, 30));
    }

    @Test
    public void deserialize_WEEK() throws IOException {
        URL url = getClass().getResource("PeriodicReportPeriodDeserializerTest_deserialize_WEEK.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(EveryWeekPeriod.class);

        EveryWeekPeriod obj = (EveryWeekPeriod) result.getProperty();
        assertThat(obj.getEvery()).isEqualTo(IntervalType.WEEK);
        assertThat(obj.getDayOfWeek()).isEqualTo(DayOfWeek.SATURDAY);
        assertThat(obj.getTime()).isEqualTo(LocalTime.of(1, 2));
    }

    @Test
    public void deserialize_DAY() throws IOException {
        URL url = getClass().getResource("PeriodicReportPeriodDeserializerTest_deserialize_DAY.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(EveryDayPeriod.class);

        EveryDayPeriod obj = (EveryDayPeriod) result.getProperty();
        assertThat(obj.getEvery()).isEqualTo(IntervalType.DAY);
        assertThat(obj.getTime()).isEqualTo(LocalTime.of(9, 0));
    }

    @Test
    public void deserialize_HOUR() throws IOException {
        URL url = getClass().getResource("PeriodicReportPeriodDeserializerTest_deserialize_HOUR.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(EveryHourPeriod.class);

        EveryHourPeriod obj = (EveryHourPeriod) result.getProperty();
        assertThat(obj.getEvery()).isEqualTo(IntervalType.HOUR);
        assertThat(obj.getMinute()).isEqualTo(0);
    }

    public static class TestObject {
        private PeriodicReportPeriod property;

        public PeriodicReportPeriod getProperty() {
            return property;
        }

        public void setProperty(PeriodicReportPeriod property) {
            this.property = property;
        }
    }
}
