package com.kintone.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.kintone.client.exception.KintoneRuntimeException;
import com.kintone.client.model.app.CustomizeFileResource;
import com.kintone.client.model.app.CustomizeResource;
import com.kintone.client.model.app.CustomizeUrlResource;
import java.io.IOException;

class CustomizeResourceDeserializer extends StdDeserializer<CustomizeResource> {
    private static final long serialVersionUID = -3264166823674337016L;

    CustomizeResourceDeserializer() {
        this(null);
    }

    CustomizeResourceDeserializer(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public CustomizeResource deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);
        String type = node.get("type").textValue();
        switch (type) {
            case "FILE":
                return codec.treeToValue(node, CustomizeFileResource.class);
            case "URL":
                return codec.treeToValue(node, CustomizeUrlResource.class);
            default:
                throw new KintoneRuntimeException("Invalid customize type: " + type);
        }
    }
}
