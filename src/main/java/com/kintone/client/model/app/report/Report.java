package com.kintone.client.model.app.report;

import java.util.List;
import lombok.Data;

/** An object containing settings of a Graph. */
@Data
public class Report {

    /**
    * The ID of the graph.
    *
    * <p>This field is ignored for the request object of Update Graphs API.
    */
    private Long id;

    /** The chart type of the graph. */
    private ChartType chartType;

    /** The display mode of the graph. */
    private ChartMode chartMode;

    /** The name of the graph. */
    private String name;

    /** The order of the graph. */
    private Long index;

    /** An array of objects containing the "Group by" options. */
    private List<AggregationGroup> groups;

    /** An array of objects containing the "Function" options. */
    private List<AggregationSetting> aggregations;

    /** The filter condition in a query format. */
    private String filterCond;

    /** An array of objects containing the "Sort by" options. */
    private List<AggregationSort> sorts;

    /** An objects containing the "Periodic Reports" options. */
    private PeriodicReport periodicReport;
}
