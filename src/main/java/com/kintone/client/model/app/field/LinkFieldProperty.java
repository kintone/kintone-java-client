package com.kintone.client.model.app.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kintone.client.model.record.FieldType;
import lombok.Data;

/**
* An object containing the properties of a Link field for getting and setting the field settings.
*/
@Data
@JsonIgnoreProperties(value = "type", allowGetters = true)
public class LinkFieldProperty implements FieldProperty {

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
    private String defaultValue;

    /** The maximum number of digits for the field. */
    private Long maxLength;

    /** The minimum number of digits for the field. */
    private Long minLength;

    /** The Link type settings for the field. */
    private LinkProtocol protocol;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.LINK;
    }
}
