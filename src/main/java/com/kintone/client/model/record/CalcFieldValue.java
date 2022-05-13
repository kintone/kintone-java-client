package com.kintone.client.model.record;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/** A value object for a Calculated field. */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode(doNotUseGetters = true)
public class CalcFieldValue implements FieldValue {

    /** The value of the Calculated field. */
    private final String value;

    public CalcFieldValue(String rawValue) {
        this.value = rawValue;
    }

    public CalcFieldValue(BigDecimal value) {
        if (value != null) {
            this.value = value.toPlainString();
        } else {
            this.value = null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.CALC;
    }

    /**
     * Returns the value of field.
     *
     * @return the value of field.
     */
    public BigDecimal getValue() {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new BigDecimal(value);
    }

    /**
     * Returns the raw value of field.
     *
     * @return the raw value of field.
     */
    public String getRawValue() {
        return value;
    }
}
