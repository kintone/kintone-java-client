package com.kintone.client.model.record;

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

    public CalcFieldValue(String value) {
        this.value = value;
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
    public String getValue() {
        return value;
    }
}
