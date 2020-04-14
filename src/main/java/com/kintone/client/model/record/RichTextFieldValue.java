package com.kintone.client.model.record;

import lombok.Value;

/** A value object for a Rich text field. */
@Value
public class RichTextFieldValue implements FieldValue {

    /** The value of the Rich text field. */
    private final String value;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.RICH_TEXT;
    }
}
