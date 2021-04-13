package com.kintone.client.model.app.report;

import lombok.Data;

/** An object containing a "Function" setting of Graphs */
@Data
public class AggregationSetting {

    /** The type of the "Function" option of the Graph. */
    private AggregationFunction type;

    /** The field code of the field used in the "Function" option of the Graph. */
    private String code;
}
