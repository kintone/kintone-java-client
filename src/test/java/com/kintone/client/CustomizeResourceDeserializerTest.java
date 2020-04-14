package com.kintone.client;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kintone.client.exception.KintoneRuntimeException;
import com.kintone.client.model.app.CustomizeFileResource;
import com.kintone.client.model.app.CustomizeResource;
import com.kintone.client.model.app.CustomizeType;
import com.kintone.client.model.app.CustomizeUrlResource;
import java.io.IOException;
import java.net.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomizeResourceDeserializerTest {

    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(CustomizeResource.class, new CustomizeResourceDeserializer());
        mapper = new ObjectMapper();
        mapper.registerModule(module);
    }

    @Test
    public void deserialize_FILE() throws IOException {
        URL url = getClass().getResource("CustomizeResourceDeserializerTest_deserialize_FILE.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getResource()).isInstanceOf(CustomizeFileResource.class);

        CustomizeFileResource obj = (CustomizeFileResource) result.getResource();
        assertThat(obj.getType()).isEqualTo(CustomizeType.FILE);
        assertThat(obj.getFile().getContentType()).isEqualTo("image/png");
        assertThat(obj.getFile().getFileKey()).isEqualTo("abcd-efgh-0123");
        assertThat(obj.getFile().getName()).isEqualTo("test.png");
        assertThat(obj.getFile().getSize()).isEqualTo(100);
    }

    @Test
    public void deserialize_URL() throws IOException {
        URL url = getClass().getResource("CustomizeResourceDeserializerTest_deserialize_URL.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getResource()).isInstanceOf(CustomizeUrlResource.class);

        CustomizeUrlResource obj = (CustomizeUrlResource) result.getResource();
        assertThat(obj.getType()).isEqualTo(CustomizeType.URL);
        assertThat(obj.getUrl()).isEqualTo("https://example.com/a/b/c.js");
    }

    @Test
    public void deserialize_invalidType() {
        URL url =
                getClass().getResource("CustomizeResourceDeserializerTest_deserialize_invalidType.json");

        Throwable thrown = catchThrowable(() -> mapper.readValue(url, TestObject.class));
        assertThat(thrown.getCause())
                .isInstanceOf(KintoneRuntimeException.class)
                .hasMessage("Invalid customize type: TEST");
    }

    public static class TestObject {
        private CustomizeResource resource;

        public CustomizeResource getResource() {
            return resource;
        }

        public void setResource(CustomizeResource resource) {
            this.resource = resource;
        }
    }
}
