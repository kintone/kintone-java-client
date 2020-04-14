package com.kintone.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.kintone.client.exception.KintoneRuntimeException;
import com.kintone.client.model.app.AppFileIcon;
import com.kintone.client.model.app.AppIcon;
import com.kintone.client.model.app.AppPresetIcon;
import java.io.IOException;

class AppIconDeserializer extends StdDeserializer<AppIcon> {
    private static final long serialVersionUID = 4465022900024070300L;

    AppIconDeserializer() {
        this(null);
    }

    AppIconDeserializer(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public AppIcon deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);
        String type = node.get("type").textValue();
        switch (type) {
            case "FILE":
                return codec.treeToValue(node, AppFileIcon.class);
            case "PRESET":
                return codec.treeToValue(node, AppPresetIcon.class);
            default:
                throw new KintoneRuntimeException("Invalid icon type: " + type);
        }
    }
}
