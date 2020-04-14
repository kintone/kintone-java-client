package com.kintone.client.model.record;

import java.time.ZonedDateTime;
import lombok.Value;

/** A value object for a Created date-time field. */
@Value
public class CreatedTimeFieldValue implements FieldValue {

    /** The created date-time of the record. */
    private final ZonedDateTime value;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.CREATED_TIME;
    }
}
