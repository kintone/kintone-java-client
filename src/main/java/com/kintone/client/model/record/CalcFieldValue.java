package com.kintone.client.model.record;

import java.math.BigDecimal;
import lombok.Value;

/** A value object for a Calculated field. */
@Value
public class CalcFieldValue implements FieldValue {

    /** The value of the Calculated field. */
    private final String value;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.CALC;
    }
}
