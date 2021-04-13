package com.kintone.client.model.app.report;

/** An interface for the "Period" options of the "Periodic Report". */
public interface PeriodicReportPeriod {

    /**
    * Get the time interval type used to determine when to generate the Periodic Reports.
    *
    * @return the time interval type
    */
    IntervalType getEvery();
}
