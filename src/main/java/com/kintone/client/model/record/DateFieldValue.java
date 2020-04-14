package com.kintone.client.model.record;

import java.time.LocalDate;
import lombok.Value;

/** A value object for a Date field. */
@Value
public class DateFieldValue implements FieldValue {

    /** The value of the Date field. */
    private final LocalDate value;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.DATE;
    }
}
