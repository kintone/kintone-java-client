package com.kintone.client.model.record;

import java.time.ZonedDateTime;
import lombok.Value;

/** A value object for an Updated date-time field. */
@Value
public class UpdatedTimeFieldValue implements FieldValue {

    /** The updated date-time of the record. */
    private final ZonedDateTime value;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.UPDATED_TIME;
    }
}
