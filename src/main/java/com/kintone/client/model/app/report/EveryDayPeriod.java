package com.kintone.client.model.app.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalTime;
import lombok.Data;

@Data
@JsonIgnoreProperties(value = "every", allowGetters = true)
public class EveryDayPeriod implements PeriodicReportPeriod {

    /** The time when the Periodic Report will be generated. */
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;

    /** {@inheritDoc} */
    @Override
    public IntervalType getEvery() {
        return IntervalType.DAY;
    }
}
