package com.kintone.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kintone.client.exception.KintoneRuntimeException;
import com.kintone.client.model.app.AppIcon;
import com.kintone.client.model.app.CustomizeResource;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.app.layout.Layout;
import com.kintone.client.model.app.report.EveryMonthPeriod;
import com.kintone.client.model.app.report.EveryQuarterPeriod;
import com.kintone.client.model.app.report.PeriodicReportPeriod;
import com.kintone.client.model.record.Record;
import java.io.IOException;
import java.io.InputStream;

class JsonMapper {
    private final ObjectMapper mapper;

    JsonMapper() {
        mapper = createObjectMapper();
    }

    private static ObjectMapper createObjectMapper() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Record.class, new RecordDeserializer());
        module.addDeserializer(FieldProperty.class, new FieldPropertyDeserializer());
        module.addDeserializer(Layout.class, new LayoutDeserializer());
        module.addDeserializer(CustomizeResource.class, new CustomizeResourceDeserializer());
        module.addDeserializer(AppIcon.class, new AppIconDeserializer());
        module.addDeserializer(PeriodicReportPeriod.class, new PeriodicReportPeriodDeserializer());
        module.addSerializer(Record.class, new RecordSerializer());
        module.addSerializer(EveryMonthPeriod.class, new EveryMonthPeriodSerializer());
        module.addSerializer(EveryQuarterPeriod.class, new EveryQuarterPeriodSerializer());

        return com.fasterxml.jackson.databind.json.JsonMapper.builder()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .addModule(module)
                .addModule(new JavaTimeModule())
                .build();
    }

    byte[] format(Object obj) {
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new KintoneRuntimeException("Failed to format request JSON", e);
        }
    }

    <T> T parse(InputStream stream, Class<T> clazz) {
        try {
            return mapper.readValue(stream, clazz);
        } catch (IOException e) {
            throw new KintoneRuntimeException("Failed to parse response JSON", e);
        }
    }

    <T> T convert(Object object, Class<T> clazz) {
        return mapper.convertValue(object, clazz);
    }
}
