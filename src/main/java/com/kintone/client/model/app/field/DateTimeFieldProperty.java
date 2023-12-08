package com.kintone.client.model.app.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kintone.client.model.record.FieldType;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.Data;

/**
 * An object containing the properties of a Date and time field for getting and setting the field
 * settings.
 */
@Data
@JsonIgnoreProperties(value = "type", allowGetters = true)
public class DateTimeFieldProperty implements FieldProperty {

    private final DateTimeFormatter DEFAULT_VALUE_FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

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

    // Kintone API returns defaultValue in the format of `yyyy-MM-dd'T'HH:mm`.
    // This value is lack of compatibility with the ISO-8601 format, so DateTimeParseException occurs like following.
    // java.time.format.DateTimeParseException: Text '2019-03-27T10:15' could not be parsed: Unable to obtain ZonedDateTime from TemporalAccessor: {},ISO resolved to 2019-03-27T10:15 of type java.time.format.Parsed
    // This is a workaround until the API returns the expected format.
    public void setDefaultValue(String defaultValue) {
        if (defaultValue.isEmpty()) {
            return;
        }
        try {
            this.defaultValue = ZonedDateTime.parse(defaultValue, DEFAULT_VALUE_FORMATTER);
        } catch (DateTimeParseException ex) {
            if (defaultValue.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
                this.defaultValue = ZonedDateTime.parse(defaultValue + ":00Z", DEFAULT_VALUE_FORMATTER);
            } else {
                throw ex;
            }
        }
    }
}
