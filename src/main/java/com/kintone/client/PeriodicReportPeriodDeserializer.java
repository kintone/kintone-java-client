package com.kintone.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.kintone.client.exception.KintoneRuntimeException;
import com.kintone.client.model.app.report.*;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class PeriodicReportPeriodDeserializer extends StdDeserializer<PeriodicReportPeriod> {

    private static final long serialVersionUID = 7683119889317941808L;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    PeriodicReportPeriodDeserializer() {
        this(null);
    }

    PeriodicReportPeriodDeserializer(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public PeriodicReportPeriod deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        String type = node.get("every").textValue();
        IntervalType intervalType;
        try {
            intervalType = IntervalType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new KintoneRuntimeException("Invalid interval type: " + type);
        }

        if (intervalType.getPropertyClass() == null) {
            throw new KintoneRuntimeException("Invalid interval type: " + type);
        }

        if (intervalType == IntervalType.MONTH) {
            return deserializeEveryMonthPeriod(node);
        } else if (intervalType == IntervalType.QUARTER) {
            return deserializeEveryQuarterPeriod(node);
        }
        return codec.treeToValue(node, intervalType.getPropertyClass());
    }

    private EveryMonthPeriod deserializeEveryMonthPeriod(JsonNode node) {
        EveryMonthPeriod period = new EveryMonthPeriod();

        String dayOfMonth = node.get("dayOfMonth").textValue();
        if (JsonConstants.END_OF_MONTH.equals(dayOfMonth)) {
            period.setEndOfMonth();
        } else if (dayOfMonth != null) {
            period.setDayOfMonth(Integer.valueOf(dayOfMonth));
        }

        String time = node.get("time").textValue();
        if (time != null) {
            period.setTime(LocalTime.parse(time, TIME_FORMATTER));
        }
        return period;
    }

    private EveryQuarterPeriod deserializeEveryQuarterPeriod(JsonNode node) {
        EveryQuarterPeriod period = new EveryQuarterPeriod();

        String pattern = node.get("pattern").textValue();
        if (pattern != null) {
            period.setPattern(QuarterlyPattern.valueOf(pattern));
        }

        String dayOfMonth = node.get("dayOfMonth").textValue();
        if (JsonConstants.END_OF_MONTH.equals(dayOfMonth)) {
            period.setEndOfMonth();
        } else if (dayOfMonth != null) {
            period.setDayOfMonth(Integer.valueOf(dayOfMonth));
        }

        String time = node.get("time").textValue();
        if (time != null) {
            period.setTime(LocalTime.parse(time, TIME_FORMATTER));
        }
        return period;
    }
}
