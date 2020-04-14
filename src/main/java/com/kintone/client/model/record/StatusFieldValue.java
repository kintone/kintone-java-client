package com.kintone.client.model.record;

import lombok.Value;

/** A value object for a Status field. */
@Value
public class StatusFieldValue implements FieldValue {

    /** The value of the Status field. */
    private final String value;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.STATUS;
    }
}
