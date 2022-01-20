package com.kintone.client.model.app.field;

import com.kintone.client.model.record.FieldType;

/** An interface for the property of fields. */
public interface FieldProperty {
    /**
     * Get the field type.
     *
     * @return field type
     */
    FieldType getType();

    /**
     * Get the field code.
     *
     * @return field code
     */
    String getCode();
}
