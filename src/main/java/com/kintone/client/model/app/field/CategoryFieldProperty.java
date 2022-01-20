package com.kintone.client.model.app.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kintone.client.model.record.FieldType;
import lombok.Data;

/** An object containing the properties of the Category field for getting the field settings. */
@Data
@JsonIgnoreProperties(value = "type", allowGetters = true)
public class CategoryFieldProperty implements FieldProperty {

    /** The field code of the field. */
    private String code;

    /** The field name. */
    private String label;

    /**
     * Whether the category feature is enabled.
     *
     * @return true if the category feature is enabled
     */
    private Boolean enabled;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.CATEGORY;
    }
}
