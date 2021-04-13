package com.kintone.client.model.app.report;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class EveryQuarterPeriodTest {

    @Test
    public void isEndOfMonth() {
        EveryQuarterPeriod period = new EveryQuarterPeriod();
        assertThat(period.isEndOfMonth()).isFalse();

        period.setEndOfMonth();
        assertThat(period.isEndOfMonth()).isTrue();

        period.setDayOfMonth(1);
        assertThat(period.isEndOfMonth()).isFalse();
    }

    @Test
    public void getDayOfMonth() {
        EveryQuarterPeriod period = new EveryQuarterPeriod();
        assertThat(period.getDayOfMonth()).isNull();

        period.setEndOfMonth();
        assertThat(period.getDayOfMonth()).isNull();

        period.setDayOfMonth(2);
        assertThat(period.getDayOfMonth()).isEqualTo(2);
    }
}
