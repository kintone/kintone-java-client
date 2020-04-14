package com.kintone.client.model.record;

import java.time.LocalTime;
import lombok.Value;

/** A value object for a Time field. */
@Value
public class TimeFieldValue implements FieldValue {

    /** The value of the Time field. */
    private final LocalTime value;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.TIME;
    }
}
