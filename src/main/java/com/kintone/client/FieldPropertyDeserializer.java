package com.kintone.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.kintone.client.exception.KintoneRuntimeException;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.app.field.LookupFieldProperty;
import com.kintone.client.model.record.FieldType;
import java.io.IOException;

class FieldPropertyDeserializer extends StdDeserializer<FieldProperty> {
    private static final long serialVersionUID = 2056843308708375298L;

    FieldPropertyDeserializer() {
        this(null);
    }

    FieldPropertyDeserializer(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public FieldProperty deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        if (node.has("lookup")) {
            return codec.treeToValue(node, LookupFieldProperty.class);
        }

        String type = node.get("type").textValue();
        FieldType fieldType;
        try {
            fieldType = FieldType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new KintoneRuntimeException("Invalid field type: " + type);
        }

        if (fieldType.getFieldPropertyClass() == null) {
            throw new KintoneRuntimeException("Invalid field type: " + type);
        }
        return codec.treeToValue(node, fieldType.getFieldPropertyClass());
    }
}
