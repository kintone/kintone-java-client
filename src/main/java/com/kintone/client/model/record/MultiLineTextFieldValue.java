package com.kintone.client.model.record;

import lombok.Value;

/** A value object for a Text Area field. */
@Value
public class MultiLineTextFieldValue implements FieldValue {

    /** The value of the Text Area field. */
    private final String value;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.MULTI_LINE_TEXT;
    }
}
