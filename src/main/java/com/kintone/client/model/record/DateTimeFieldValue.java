package com.kintone.client.model.record;

import java.time.ZonedDateTime;
import lombok.Value;

/** A value object for a Date and time field. */
@Value
public class DateTimeFieldValue implements FieldValue {

    /** The value of the Date and time field. */
    private final ZonedDateTime value;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.DATETIME;
    }
}
