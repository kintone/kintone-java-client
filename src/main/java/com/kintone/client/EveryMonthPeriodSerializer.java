package com.kintone.client;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.kintone.client.model.app.report.EveryMonthPeriod;
import com.kintone.client.model.app.report.IntervalType;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

class EveryMonthPeriodSerializer extends StdSerializer<EveryMonthPeriod> {

    private static final long serialVersionUID = 3456186636225393689L;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    EveryMonthPeriodSerializer() {
        this(null);
    }

    protected EveryMonthPeriodSerializer(Class<EveryMonthPeriod> t) {
        super(t);
    }

    @Override
    public void serialize(EveryMonthPeriod value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeStartObject();
        gen.writeStringField("every", IntervalType.MONTH.toString());
        if (value.isEndOfMonth()) {
            gen.writeStringField("dayOfMonth", JsonConstants.END_OF_MONTH);
        } else {
            writeField(gen, "dayOfMonth", value.getDayOfMonth(), v -> Integer.toString(v));
        }
        writeField(gen, "time", value.getTime(), v -> v.format(TIME_FORMATTER));
        gen.writeEndObject();
    }

    private <T> void writeField(JsonGenerator gen, String field, T value, Function<T, String> func)
            throws IOException {
        if (value == null) {
            gen.writeNullField(field);
        } else {
            gen.writeStringField(field, func.apply(value));
        }
    }
}
