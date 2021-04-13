package com.kintone.client.model.app.report;

import lombok.Data;

/** An object containing a "Group by" setting of Graphs */
@Data
public class AggregationGroup {

    /** The field code of the field used to determine the "Group by" option. */
    private String code;

    /** The time unit used for the "Group by" option. */
    private AggregationTimeUnit per;
}
