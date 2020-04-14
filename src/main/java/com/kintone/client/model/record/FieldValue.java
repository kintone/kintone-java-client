package com.kintone.client.model.record;

/** An interface represents the field values of {@link Record}. */
public interface FieldValue {

    /**
    * Returns the type of field.
    *
    * @return the type of field.
    */
    FieldType getType();

    /**
    * States whether the field is a built-in field.
    *
    * @return true if the field is a built-in field.
    */
    default boolean isBuiltin() {
        return getType().isBuiltin();
    }
}
