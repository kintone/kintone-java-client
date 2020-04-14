package com.kintone.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.kintone.client.exception.KintoneRuntimeException;
import com.kintone.client.model.app.layout.GroupLayout;
import com.kintone.client.model.app.layout.Layout;
import com.kintone.client.model.app.layout.RowLayout;
import com.kintone.client.model.app.layout.SubtableLayout;
import java.io.IOException;

class LayoutDeserializer extends StdDeserializer<Layout> {
    private static final long serialVersionUID = 7393979541628669470L;

    LayoutDeserializer() {
        this(null);
    }

    LayoutDeserializer(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public Layout deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);
        String type = node.get("type").textValue();
        switch (type) {
            case "ROW":
                return codec.treeToValue(node, RowLayout.class);
            case "SUBTABLE":
                return codec.treeToValue(node, SubtableLayout.class);
            case "GROUP":
                return codec.treeToValue(node, GroupLayout.class);
            default:
                throw new KintoneRuntimeException("Invalid layout type: " + type);
        }
    }
}
