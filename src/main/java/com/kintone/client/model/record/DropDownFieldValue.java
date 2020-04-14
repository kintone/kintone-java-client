package com.kintone.client.model.record;

import lombok.Value;

/** A value object for a Drop-down field. */
@Value
public class DropDownFieldValue implements FieldValue {

    /** The selected option of the Drop-down. */
    private final String value;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.DROP_DOWN;
    }
}
