package com.kintone.client.model.app.field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.kintone.client.model.record.FieldType;
import java.io.IOException;
import java.time.LocalTime;
import lombok.Data;

/**
 * An object containing the properties of a Time field for getting and setting the field settings.
 */
@Data
@JsonIgnoreProperties(value = "type", allowGetters = true)
public class TimeFieldProperty implements FieldProperty {

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

    /** The default value. */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @JsonDeserialize(using = DefaultValueDeserializer.class)
    private LocalTime defaultValue;

    /** The "Default to the record creation date" option. */
    private Boolean defaultNowValue;

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.TIME;
    }

    private static class DefaultValueDeserializer extends StdDeserializer<LocalTime> {

        private static final long serialVersionUID = 1;

        public DefaultValueDeserializer() {
            this(null);
        }

        protected DefaultValueDeserializer(Class<?> clazz) {
            super(clazz);
        }

        @Override
        public LocalTime deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JacksonException {
            JsonNode node = jp.getCodec().readTree(jp);
            String textVal = node.textValue();
            return textVal.isEmpty() ? null : LocalTime.parse(textVal);
        }
    }
}
