package com.kintone.client.model.record;

import lombok.Value;

/** A value object for a Link field. */
@Value
public class LinkFieldValue implements FieldValue {

    /** The value of the Link field. */
    private final String value;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.LINK;
    }
}
