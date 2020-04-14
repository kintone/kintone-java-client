package com.kintone.client.model.record;

import java.math.BigDecimal;
import lombok.Value;

/** A value object for a Number field. */
@Value
public class NumberFieldValue implements FieldValue {

    /** The value of the Number field. */
    private final BigDecimal value;

    public NumberFieldValue(BigDecimal value) {
        this.value = value;
    }

    public NumberFieldValue(long value) {
        this.value = BigDecimal.valueOf(value);
    }

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.NUMBER;
    }
}
