package com.kintone.client.model.app.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kintone.client.model.record.FieldType;
import lombok.Data;

/**
 * An object containing the properties of the Created date-time field for getting and setting the
 * field settings.
 */
@Data
@JsonIgnoreProperties(value = "type", allowGetters = true)
public class CreatedTimeFieldProperty implements FieldProperty {

    /** The field code of the field. */
    private String code;

    /** The field name. */
    private String label;

    /** The "Hide field name" option. */
    private Boolean noLabel;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.CREATED_TIME;
    }
}
