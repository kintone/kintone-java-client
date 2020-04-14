package com.kintone.client.model.record;

import lombok.Value;

/** A value object for a Record number field. */
@Value
public class RecordNumberFieldValue implements FieldValue {

    /** The value of the Record number field. */
    private final String value;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.RECORD_NUMBER;
    }
}
