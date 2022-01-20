package com.kintone.client.model.app.report;

/** An enum for representing the display mode of Graphs. */
public enum ChartMode {

    /**
     * NORMAL refers to "Clustered graph" for BAR or COLUMN chart type, or "Non-stacked graph" for
     * AREA or SPLINE_AREA chart type.
     */
    NORMAL,

    /** Stacked graph */
    STACKED,

    /** 100% stacked graph */
    PERCENTAGE
}
