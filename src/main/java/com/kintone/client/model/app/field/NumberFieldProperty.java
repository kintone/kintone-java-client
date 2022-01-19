package com.kintone.client.model.app.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kintone.client.model.record.FieldType;
import java.math.BigDecimal;
import lombok.Data;

/**
 * An object containing the properties of a Number field for getting and setting the field settings.
 */
@Data
@JsonIgnoreProperties(value = "type", allowGetters = true)
public class NumberFieldProperty implements FieldProperty {

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

    /**
     * The "Prohibit duplicate values" option.
     *
     * @return true if duplicate values will be prohibited.
     */
    private Boolean unique;

    /** The maximum number of characters for the field */
    private BigDecimal maxValue;

    /** The minimum number of characters for the field. */
    private BigDecimal minValue;

    /** The default value. */
    private BigDecimal defaultValue;

    /**
     * The "Use thousands separators" option.
     *
     * @return true if thousands separators will be displayed for the number.
     */
    private Boolean digit;

    /** The number of decimal places to display for the field. */
    private Long displayScale;

    /** The Currency settings of the field. */
    private String unit;

    /** The display position of the Currency. */
    private UnitPosition unitPosition;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.NUMBER;
    }
}
