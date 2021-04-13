package com.kintone.client.model.app.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(value = "every", allowGetters = true)
public class EveryHourPeriod implements PeriodicReportPeriod {

    /** The minute when the hourly Periodic Report will be generated. */
    private Integer minute;

    /** {@inheritDoc} */
    @Override
    public IntervalType getEvery() {
        return IntervalType.HOUR;
    }
}
