package com.kintone.client.model.record;

import com.kintone.client.model.User;
import lombok.Value;

/** A value object for a Creator field. */
@Value
public class CreatorFieldValue implements FieldValue {

    /** The user who created the record. */
    private final User value;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.CREATOR;
    }
}
