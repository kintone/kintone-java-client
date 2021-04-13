package com.kintone.client.model.app.report;

import com.kintone.client.model.Order;
import lombok.Data;

/** An object containing a "Sort by" setting of Graphs */
@Data
public class AggregationSort {

    /** The setting how the graph is sorted. */
    private AggregationSortTarget by;

    /** The order of sorting. */
    private Order order;
}
