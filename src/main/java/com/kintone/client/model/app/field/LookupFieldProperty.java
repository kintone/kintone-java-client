package com.kintone.client.model.app.field;

import com.kintone.client.model.record.FieldType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An object containing the properties of a Lookup field for getting and setting the field settings.
 */
@Data
@NoArgsConstructor
public class LookupFieldProperty implements FieldProperty {

    /** The field code of the field. */
    private String code;

    /** The field type. */
    private FieldType type;

    /** The field name. */
    private String label;

    /**
     * The "Hide field name" option.
     *
     * @return true if the field's name will be hidden
     */
    private Boolean noLabel;

    /**
     * The "Required field" option.
     *
     * @return true if the field will be a required field.
     */
    private Boolean required;

    /** An object containing the settings of the Lookup field. */
    private LookupSetting lookup;

    public LookupFieldProperty(FieldType type) {
        this.setType(type);
    }
}
