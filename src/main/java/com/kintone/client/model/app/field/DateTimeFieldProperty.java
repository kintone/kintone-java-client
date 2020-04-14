package com.kintone.client.model.app.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kintone.client.model.record.FieldType;
import java.time.ZonedDateTime;
import lombok.Data;

/**
* An object containing the properties of a Date and time field for getting and setting the field
* settings.
*/
@Data
@JsonIgnoreProperties(value = "type", allowGetters = true)
public class DateTimeFieldProperty implements FieldProperty {

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

    /** The default value. */
    private ZonedDateTime defaultValue;

    /**
    * The "Default to the record creation date" option.
    *
    * @return true if the record creation date will be used as the default value.
    */
    private Boolean defaultNowValue;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.DATETIME;
    }
}
