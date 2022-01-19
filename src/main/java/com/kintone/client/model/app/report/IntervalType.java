package com.kintone.client.model.app.report;

import lombok.Getter;

/**
 * An enum for representing the time interval types used to determine when to generate the Periodic
 * Reports.
 */
public enum IntervalType {

    /** Generates the Periodic Reports every year */
    YEAR(EveryYearPeriod.class),

    /** Generates the Periodic Reports every quarter */
    QUARTER(EveryQuarterPeriod.class),

    /** Generates the Periodic Reports every month */
    MONTH(EveryMonthPeriod.class),

    /** Generates the Periodic Reports every week */
    WEEK(EveryWeekPeriod.class),

    /** Generates the Periodic Reports every day */
    DAY(EveryDayPeriod.class),

    /** Generates the Periodic Reports every hour */
    HOUR(EveryHourPeriod.class);

    @Getter private final Class<? extends PeriodicReportPeriod> propertyClass;

    IntervalType(Class<? extends PeriodicReportPeriod> propertyClass) {
        this.propertyClass = propertyClass;
    }
}
