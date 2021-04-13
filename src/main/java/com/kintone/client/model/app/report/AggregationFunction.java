package com.kintone.client.model.app.report;

/** An enum for representing the type of the "Function" option of an aggregation. */
public enum AggregationFunction {

    /** calculate the number of records */
    COUNT,

    /** calculate the total of the field values. */
    SUM,

    /** calculate the average of the field values. */
    AVERAGE,

    /** calculate the maximum value of the field. */
    MAX,

    /** calculate the minimum value of the field. */
    MIN
}
