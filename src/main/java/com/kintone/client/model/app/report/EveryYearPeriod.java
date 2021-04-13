package com.kintone.client.model.app.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalTime;
import lombok.Data;

@Data
@JsonIgnoreProperties(value = "every", allowGetters = true)
public class EveryYearPeriod implements PeriodicReportPeriod {

    /**
    * The month when the Periodic Report will be generated. The month is returned as an integer,
    * ranging from 1 (January) to 12 (December).
    */
    private Integer month;

    /**
    * The day when the Periodic Report will be generated. The day is returned as an integer, ranging
    * from 1 to 31.
    */
    private Integer dayOfMonth;

    /** The time when the Periodic Report will be generated. */
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;

    /** {@inheritDoc} */
    @Override
    public IntervalType getEvery() {
        return IntervalType.YEAR;
    }
}
