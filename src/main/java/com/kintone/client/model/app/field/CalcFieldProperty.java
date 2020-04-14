package com.kintone.client.model.app.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kintone.client.model.record.FieldType;
import lombok.Data;

/**
* An object containing the properties of a Calculated field for getting and setting the field
* settings.
*/
@Data
@JsonIgnoreProperties(value = "type", allowGetters = true)
public class CalcFieldProperty implements FieldProperty {

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

    /**
    * The "Required field" option.
    *
    * @return true if the field will be a required field.
    */
    private Boolean required;

    /** The formula expression used in the field. */
    private String expression;

    /** The display format for fields with calculations */
    private DisplayFormat format;

    /** The number of decimal places to display for the field. */
    private Long displayScale;

    /**
    * The "Hide formula" settings for the field.
    *
    * @return true if the formula will be hidden.
    */
    private Boolean hideExpression;

    /** The currency settings of the field. */
    private String unit;

    /** The display position of the currency. */
    private UnitPosition unitPosition;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.CALC;
    }
}
