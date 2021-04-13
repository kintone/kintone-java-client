package com.kintone.client.model.app.report;

import lombok.Data;

/** An object containing the "Periodic Reports" options of a Graph. */
@Data
public class PeriodicReport {

    /** The activation status of the "Periodic Reports" option. */
    private Boolean active;

    /** An object containing the "Period" options. */
    private PeriodicReportPeriod period;
}
