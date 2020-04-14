package com.kintone.client.model.record;

import com.kintone.client.model.User;
import lombok.Value;

/** A value object for a Modifier field. */
@Value
public class ModifierFieldValue implements FieldValue {

    /** The user who updated the record. */
    private final User value;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.MODIFIER;
    }
}
