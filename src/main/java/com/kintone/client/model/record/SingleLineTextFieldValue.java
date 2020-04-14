package com.kintone.client.model.record;

import lombok.Value;

/** A value object for a Text field. */
@Value
public class SingleLineTextFieldValue implements FieldValue {

    /** The value of the Text field. */
    private final String value;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.SINGLE_LINE_TEXT;
    }
}
