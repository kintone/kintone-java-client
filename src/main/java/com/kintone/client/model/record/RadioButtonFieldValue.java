package com.kintone.client.model.record;

import lombok.Value;

/** A value object for a Radio button field. */
@Value
public class RadioButtonFieldValue implements FieldValue {

    /** The selected option of the Radio button. */
    private final String value;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.RADIO_BUTTON;
    }
}
