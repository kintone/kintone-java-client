package com.kintone.client.model.app.report;

import java.time.LocalTime;
import lombok.*;

@ToString
@EqualsAndHashCode
public class EveryMonthPeriod implements PeriodicReportPeriod {

    private static final String END_OF_MONTH = "END_OF_MONTH";

    /**
     * The day when the Periodic Report will be generated. The day is returned as an integer, ranging
     * from 1 to 31, or set as "END_OF_MONTH".
     */
    private String dayOfMonth;

    /** The time when the Periodic Report will be generated. */
    @Getter @Setter private LocalTime time;

    /** {@inheritDoc} */
    @Override
    public IntervalType getEvery() {
        return IntervalType.MONTH;
    }

    /**
     * Checks whether the day when the Periodic Report will be generated is set to "END_OF_MONTH".
     *
     * @return returns true if the day when the Periodic Report will be generated is set to
     *     "END_OF_MONTH".
     */
    public boolean isEndOfMonth() {
        return END_OF_MONTH.equals(dayOfMonth);
    }

    /**
     * Gets the setting of day when the Periodic Report will be generated.
     *
     * @return the day when the Periodic Report will be generated. If the setting is not set or set as
     *     "END_OF_MONTH", the result is null.
     */
    public Integer getDayOfMonth() {
        if (dayOfMonth == null || isEndOfMonth()) {
            return null;
        }
        return Integer.valueOf(dayOfMonth);
    }

    /**
     * Sets the setting of day when the Periodic Report will be generated. To set the setting to
     * "END_OF_MONTH", use {@link #setEndOfMonth()}.
     *
     * @param dayOfMonth the day of Month
     * @return this object
     */
    public EveryMonthPeriod setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = Integer.toString(dayOfMonth);
        return this;
    }

    /**
     * Sets the setting of day when the Periodic Report will be generated to "END_OF_MONTH".
     *
     * @return this object
     */
    public EveryMonthPeriod setEndOfMonth() {
        this.dayOfMonth = END_OF_MONTH;
        return this;
    }
}
