package com.kintone.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kintone.client.exception.KintoneRuntimeException;
import com.kintone.client.model.app.AppFileIcon;
import com.kintone.client.model.app.AppIcon;
import com.kintone.client.model.app.AppIconType;
import com.kintone.client.model.app.AppPresetIcon;
import java.io.IOException;
import java.net.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppIconDeserializerTest {

    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(AppIcon.class, new AppIconDeserializer());
        mapper = new ObjectMapper();
        mapper.registerModule(module);
    }

    @Test
    public void deserialize_FILE() throws IOException {
        URL url = getClass().getResource("AppIconDeserializerTest_deserialize_FILE.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getIcon()).isInstanceOf(AppFileIcon.class);

        AppFileIcon obj = (AppFileIcon) result.getIcon();
        assertThat(obj.getType()).isEqualTo(AppIconType.FILE);
        assertThat(obj.getFile().getContentType()).isEqualTo("image/png");
        assertThat(obj.getFile().getFileKey()).isEqualTo("abcd-efgh-0123");
        assertThat(obj.getFile().getName()).isEqualTo("test.png");
        assertThat(obj.getFile().getSize()).isEqualTo(100);
    }

    @Test
    public void deserialize_PRESET() throws IOException {
        URL url = getClass().getResource("AppIconDeserializerTest_deserialize_PRESET.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getIcon()).isInstanceOf(AppPresetIcon.class);

        AppPresetIcon obj = (AppPresetIcon) result.getIcon();
        assertThat(obj.getType()).isEqualTo(AppIconType.PRESET);
        assertThat(obj.getKey()).isEqualTo("DEFAULT");
    }

    @Test
    public void deserialize_invalidType() {
        URL url = getClass().getResource("AppIconDeserializerTest_deserialize_invalidType.json");

        Throwable thrown = catchThrowable(() -> mapper.readValue(url, TestObject.class));
        assertThat(thrown.getCause())
                .isInstanceOf(KintoneRuntimeException.class)
                .hasMessage("Invalid icon type: TEST");
    }

    public static class TestObject {
        private AppIcon icon;

        public AppIcon getIcon() {
            return icon;
        }

        public void setResource(AppIcon icon) {
            this.icon = icon;
        }
    }
}
