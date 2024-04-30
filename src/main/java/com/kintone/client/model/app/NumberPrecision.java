package com.kintone.client.model.app;

import lombok.Data;

/** An object containing the precision settings of numbers and calculations */
@Data
public class NumberPrecision {

    /** The total number of digits. */
    private Integer digits;

    /** The number of decimal places to round. */
    private Integer decimalPlaces;

    /** The rounding mode of the Number and Calculated fields. */
    private RoundingMode roundingMode;
}
