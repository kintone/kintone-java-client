package com.kintone.client.model.app.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kintone.client.model.record.FieldType;
import lombok.Data;

/**
* An object containing the properties of a Related Records field for getting and setting the field
* settings.
*/
@Data
@JsonIgnoreProperties(value = "type", allowGetters = true)
public class ReferenceTableFieldProperty implements FieldProperty {

    /** The field code of the field. */
    private String code;

    /** The field name. */
    private String label;

    /**
    * The "Hide field name" option.
    *
    * @return true if the field's name will be hidden
    */
    private Boolean noLabel;

    /** An object containing the settings of the Related Records field. */
    private ReferenceTable referenceTable;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.REFERENCE_TABLE;
    }
}
