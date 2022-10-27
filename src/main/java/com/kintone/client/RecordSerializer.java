package com.kintone.client;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.kintone.client.exception.KintoneRuntimeException;
import com.kintone.client.model.FileBody;
import com.kintone.client.model.Group;
import com.kintone.client.model.Organization;
import com.kintone.client.model.User;
import com.kintone.client.model.record.FieldType;
import com.kintone.client.model.record.Record;
import com.kintone.client.model.record.TableRow;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

class RecordSerializer extends StdSerializer<Record> {
    private static final long serialVersionUID = 8267967360812294563L;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm'Z'");

    private static final Set<FieldType> IGNORE_FIELD_TYPES;

    static {
        Set<FieldType> types = new HashSet<>();
        types.add(FieldType.CALC);
        types.add(FieldType.REFERENCE_TABLE);
        types.add(FieldType.GROUP);
        types.add(FieldType.CATEGORY);
        types.add(FieldType.STATUS);
        types.add(FieldType.STATUS_ASSIGNEE);
        types.add(FieldType.RECORD_NUMBER);
        types.add(FieldType.SPACER);
        types.add(FieldType.LABEL);
        types.add(FieldType.HR);
        types.add(FieldType.__ID__);
        types.add(FieldType.__REVISION__);
        IGNORE_FIELD_TYPES = Collections.unmodifiableSet(types);
    }

    RecordSerializer() {
        this(null);
    }

    RecordSerializer(Class<Record> t) {
        super(t);
    }

    @Override
    public void serialize(Record record, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeStartObject();
        for (String fieldCode : record.getFieldCodes(true)) {
            FieldType type = record.getFieldType(fieldCode);
            if (!IGNORE_FIELD_TYPES.contains(type)) {
                writeValue(record, fieldCode, type, gen);
            }
        }
        gen.writeEndObject();
    }

    private void writeValue(Record record, String fieldCode, FieldType type, JsonGenerator gen)
            throws IOException {
        gen.writeObjectFieldStart(fieldCode);

        switch (type) {
            case CHECK_BOX:
                {
                    List<String> values = record.getCheckBoxFieldValue(fieldCode);
                    writeArray(gen, values);
                    break;
                }
            case CREATED_TIME:
                {
                    ZonedDateTime value = record.getCreatedTimeFieldValue();
                    writeString(gen, value, v -> v.format(DATETIME_FORMATTER));
                    break;
                }
            case CREATOR:
                {
                    User value = record.getCreatorFieldValue();
                    writeUser(gen, value);
                    break;
                }
            case DATE:
                {
                    LocalDate value = record.getDateFieldValue(fieldCode);
                    writeString(gen, value, v -> v.format(DATE_FORMATTER));
                    break;
                }
            case DATETIME:
                {
                    ZonedDateTime value = record.getDateTimeFieldValue(fieldCode);
                    writeString(gen, value, v -> v.format(DATETIME_FORMATTER));
                    break;
                }
            case DROP_DOWN:
                {
                    String value = record.getDropDownFieldValue(fieldCode);
                    gen.writeStringField("value", value);
                    break;
                }
            case FILE:
                {
                    List<FileBody> values = record.getFileFieldValue(fieldCode);
                    writeObjects(gen, "fileKey", values, FileBody::getFileKey);
                    break;
                }
            case GROUP_SELECT:
                {
                    List<Group> values = record.getGroupSelectFieldValue(fieldCode);
                    writeObjects(gen, "code", values, Group::getCode);
                    break;
                }
            case LINK:
                {
                    String value = record.getLinkFieldValue(fieldCode);
                    gen.writeStringField("value", value);
                    break;
                }
            case MODIFIER:
                {
                    User value = record.getModifierFieldValue();
                    writeUser(gen, value);
                    break;
                }
            case MULTI_LINE_TEXT:
                {
                    String value = record.getMultiLineTextFieldValue(fieldCode);
                    gen.writeStringField("value", value);
                    break;
                }
            case MULTI_SELECT:
                {
                    List<String> values = record.getMultiSelectFieldValue(fieldCode);
                    writeArray(gen, values);
                    break;
                }
            case NUMBER:
                {
                    BigDecimal value = record.getNumberFieldValue(fieldCode);
                    writeString(gen, value, BigDecimal::toPlainString);
                    break;
                }
            case ORGANIZATION_SELECT:
                {
                    List<Organization> values = record.getOrganizationSelectFieldValue(fieldCode);
                    writeObjects(gen, "code", values, Organization::getCode);
                    break;
                }
            case RADIO_BUTTON:
                {
                    String value = record.getRadioButtonFieldValue(fieldCode);
                    gen.writeStringField("value", value);
                    break;
                }
            case RICH_TEXT:
                {
                    String value = record.getRichTextFieldValue(fieldCode);
                    gen.writeStringField("value", value);
                    break;
                }
            case SINGLE_LINE_TEXT:
                {
                    String value = record.getSingleLineTextFieldValue(fieldCode);
                    gen.writeStringField("value", value);
                    break;
                }
            case SUBTABLE:
                {
                    List<TableRow> values = record.getSubtableFieldValue(fieldCode);
                    writeSubtable(gen, fieldCode, values);
                    break;
                }
            case TIME:
                {
                    LocalTime value = record.getTimeFieldValue(fieldCode);
                    writeString(gen, value, v -> v.format(TIME_FORMATTER));
                    break;
                }
            case UPDATED_TIME:
                {
                    ZonedDateTime value = record.getUpdatedTimeFieldValue();
                    writeString(gen, value, v -> v.format(DATETIME_FORMATTER));
                    break;
                }
            case USER_SELECT:
                {
                    List<User> values = record.getUserSelectFieldValue(fieldCode);
                    writeObjects(gen, "code", values, User::getCode);
                    break;
                }
            default:
                throw new KintoneRuntimeException("Invalid field: " + fieldCode);
        }
        gen.writeEndObject();
    }

    private <T> void writeString(JsonGenerator gen, T value, Function<T, String> func)
            throws IOException {
        if (value == null) {
            gen.writeNullField("value");
        } else {
            gen.writeStringField("value", func.apply(value));
        }
    }

    private <T> void writeObjects(
            JsonGenerator gen, String name, Collection<T> values, Function<T, String> func)
            throws IOException {
        gen.writeArrayFieldStart("value");
        for (T value : values) {
            gen.writeStartObject();
            gen.writeStringField(name, func.apply(value));
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }

    private void writeArray(JsonGenerator gen, Collection<String> values) throws IOException {
        gen.writeArrayFieldStart("value");
        for (String value : values) {
            gen.writeString(value);
        }
        gen.writeEndArray();
    }

    private void writeUser(JsonGenerator gen, User user) throws IOException {
        if (user == null) {
            gen.writeNullField("value");
        } else {
            gen.writeObjectFieldStart("value");
            gen.writeStringField("code", user.getCode());
            gen.writeEndObject();
        }
    }

    private void writeSubtable(JsonGenerator gen, String fieldCode, List<TableRow> rows)
            throws IOException {
        gen.writeArrayFieldStart("value");
        for (TableRow row : rows) {
            gen.writeStartObject();
            gen.writeObjectFieldStart("value");
            writeTableRow(row, gen);
            gen.writeEndObject();
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }

    private void writeTableRow(TableRow row, JsonGenerator gen) throws IOException {
        for (String fieldCode : row.getFieldCodes()) {
            gen.writeObjectFieldStart(fieldCode);
            switch (row.getFieldType(fieldCode)) {
                case CHECK_BOX:
                    {
                        List<String> values = row.getCheckBoxFieldValue(fieldCode);
                        writeArray(gen, values);
                        break;
                    }
                case DATE:
                    {
                        LocalDate value = row.getDateFieldValue(fieldCode);
                        writeString(gen, value, v -> v.format(DATE_FORMATTER));
                        break;
                    }
                case DATETIME:
                    {
                        ZonedDateTime value = row.getDateTimeFieldValue(fieldCode);
                        writeString(gen, value, v -> v.format(DATETIME_FORMATTER));
                        break;
                    }
                case DROP_DOWN:
                    {
                        String value = row.getDropDownFieldValue(fieldCode);
                        gen.writeStringField("value", value);
                        break;
                    }
                case FILE:
                    {
                        List<FileBody> values = row.getFileFieldValue(fieldCode);
                        writeObjects(gen, "fileKey", values, FileBody::getFileKey);
                        break;
                    }
                case GROUP_SELECT:
                    {
                        List<Group> values = row.getGroupSelectFieldValue(fieldCode);
                        writeObjects(gen, "code", values, Group::getCode);
                        break;
                    }
                case LINK:
                    {
                        String value = row.getLinkFieldValue(fieldCode);
                        gen.writeStringField("value", value);
                        break;
                    }
                case MULTI_LINE_TEXT:
                    {
                        String value = row.getMultiLineTextFieldValue(fieldCode);
                        gen.writeStringField("value", value);
                        break;
                    }
                case MULTI_SELECT:
                    {
                        List<String> values = row.getMultiSelectFieldValue(fieldCode);
                        writeArray(gen, values);
                        break;
                    }
                case NUMBER:
                    {
                        BigDecimal value = row.getNumberFieldValue(fieldCode);
                        writeString(gen, value, BigDecimal::toPlainString);
                        break;
                    }
                case ORGANIZATION_SELECT:
                    {
                        List<Organization> values = row.getOrganizationSelectFieldValue(fieldCode);
                        writeObjects(gen, "code", values, Organization::getCode);
                        break;
                    }
                case RADIO_BUTTON:
                    {
                        String value = row.getRadioButtonFieldValue(fieldCode);
                        gen.writeStringField("value", value);
                        break;
                    }
                case RICH_TEXT:
                    {
                        String value = row.getRichTextFieldValue(fieldCode);
                        gen.writeStringField("value", value);
                        break;
                    }
                case SINGLE_LINE_TEXT:
                    {
                        String value = row.getSingleLineTextFieldValue(fieldCode);
                        gen.writeStringField("value", value);
                        break;
                    }
                case TIME:
                    {
                        LocalTime value = row.getTimeFieldValue(fieldCode);
                        writeString(gen, value, v -> v.format(TIME_FORMATTER));
                        break;
                    }
                case USER_SELECT:
                    {
                        List<User> values = row.getUserSelectFieldValue(fieldCode);
                        writeObjects(gen, "code", values, User::getCode);
                        break;
                    }
                default:
                    throw new KintoneRuntimeException("Invalid field: " + fieldCode);
            }
            gen.writeEndObject();
        }
    }
}
