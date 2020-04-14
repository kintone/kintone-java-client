package com.kintone.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kintone.client.exception.KintoneRuntimeException;
import com.kintone.client.model.app.layout.FieldLayout;
import com.kintone.client.model.app.layout.FieldSize;
import com.kintone.client.model.app.layout.GroupLayout;
import com.kintone.client.model.app.layout.Layout;
import com.kintone.client.model.app.layout.LayoutType;
import com.kintone.client.model.app.layout.RowLayout;
import com.kintone.client.model.app.layout.SubtableLayout;
import com.kintone.client.model.record.FieldType;
import java.io.IOException;
import java.net.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LayoutDeserializerTest {

    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Layout.class, new LayoutDeserializer());
        mapper = new ObjectMapper();
        mapper.registerModule(module);
    }

    @Test
    public void deserialize_ROW() throws IOException {
        URL url = getClass().getResource("LayoutDeserializerTest_deserialize_ROW.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getLayout()).isInstanceOf(RowLayout.class);

        RowLayout obj = (RowLayout) result.getLayout();
        assertThat(obj.getType()).isEqualTo(LayoutType.ROW);

        FieldLayout label =
                new FieldLayout()
                        .setType(FieldType.LABEL)
                        .setLabel("Label")
                        .setSize(new FieldSize().setWidth(100));
        FieldLayout spacer =
                new FieldLayout()
                        .setType(FieldType.SPACER)
                        .setElementId("test")
                        .setSize(new FieldSize().setWidth(120).setHeight(80));
        FieldLayout hr = new FieldLayout().setType(FieldType.HR).setSize(new FieldSize().setWidth(150));
        FieldLayout text =
                new FieldLayout()
                        .setType(FieldType.MULTI_LINE_TEXT)
                        .setCode("text_area")
                        .setSize(new FieldSize().setWidth(320).setInnerHeight(125));
        assertThat(obj.getFields()).containsExactly(label, spacer, hr, text);
    }

    @Test
    public void deserialize_SUBTABLE() throws IOException {
        URL url = getClass().getResource("LayoutDeserializerTest_deserialize_SUBTABLE.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getLayout()).isInstanceOf(SubtableLayout.class);

        SubtableLayout obj = (SubtableLayout) result.getLayout();
        assertThat(obj.getType()).isEqualTo(LayoutType.SUBTABLE);
        assertThat(obj.getCode()).isEqualTo("table");

        FieldLayout text =
                new FieldLayout()
                        .setType(FieldType.SINGLE_LINE_TEXT)
                        .setCode("text")
                        .setSize(new FieldSize().setWidth(100));
        FieldLayout number =
                new FieldLayout()
                        .setType(FieldType.NUMBER)
                        .setCode("number")
                        .setSize(new FieldSize().setWidth(200));
        assertThat(obj.getFields()).containsExactly(text, number);
    }

    @Test
    public void deserialize_GROUP() throws IOException {
        URL url = getClass().getResource("LayoutDeserializerTest_deserialize_GROUP.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getLayout()).isInstanceOf(GroupLayout.class);

        GroupLayout obj = (GroupLayout) result.getLayout();
        assertThat(obj.getType()).isEqualTo(LayoutType.GROUP);
        assertThat(obj.getCode()).isEqualTo("group");

        FieldLayout recordNumber =
                new FieldLayout()
                        .setType(FieldType.RECORD_NUMBER)
                        .setCode("record_number")
                        .setSize(new FieldSize().setWidth(120));
        FieldLayout createdTime =
                new FieldLayout()
                        .setType(FieldType.CREATED_TIME)
                        .setCode("created_time")
                        .setSize(new FieldSize().setWidth(200));
        assertThat(obj.getLayout()).hasSize(2);
        assertThat(obj.getLayout().get(0).getType()).isEqualTo(LayoutType.ROW);
        assertThat(((RowLayout) obj.getLayout().get(0)).getFields()).containsExactly(recordNumber);
        assertThat(obj.getLayout().get(1).getType()).isEqualTo(LayoutType.ROW);
        assertThat(((RowLayout) obj.getLayout().get(1)).getFields()).containsExactly(createdTime);
    }

    @Test
    public void deserialize_invalidType() throws IOException {
        URL url = getClass().getResource("LayoutDeserializerTest_deserialize_invalidType.json");

        Throwable thrown = catchThrowable(() -> mapper.readValue(url, TestObject.class));
        assertThat(thrown.getCause())
                .isInstanceOf(KintoneRuntimeException.class)
                .hasMessage("Invalid layout type: TEST");
    }

    public static class TestObject {
        private Layout layout;

        public Layout getLayout() {
            return layout;
        }

        public void setLayout(Layout layout) {
            this.layout = layout;
        }
    }
}
