package com.kintone.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.kintone.client.exception.KintoneRuntimeException;
import com.kintone.client.model.FileBody;
import com.kintone.client.model.Group;
import com.kintone.client.model.Organization;
import com.kintone.client.model.User;
import com.kintone.client.model.record.CalcFieldValue;
import com.kintone.client.model.record.CategoryFieldValue;
import com.kintone.client.model.record.CheckBoxFieldValue;
import com.kintone.client.model.record.CreatedTimeFieldValue;
import com.kintone.client.model.record.CreatorFieldValue;
import com.kintone.client.model.record.DateFieldValue;
import com.kintone.client.model.record.DateTimeFieldValue;
import com.kintone.client.model.record.DropDownFieldValue;
import com.kintone.client.model.record.FieldType;
import com.kintone.client.model.record.FieldValue;
import com.kintone.client.model.record.FileFieldValue;
import com.kintone.client.model.record.GroupSelectFieldValue;
import com.kintone.client.model.record.LinkFieldValue;
import com.kintone.client.model.record.ModifierFieldValue;
import com.kintone.client.model.record.MultiLineTextFieldValue;
import com.kintone.client.model.record.MultiSelectFieldValue;
import com.kintone.client.model.record.NumberFieldValue;
import com.kintone.client.model.record.OrganizationSelectFieldValue;
import com.kintone.client.model.record.RadioButtonFieldValue;
import com.kintone.client.model.record.Record;
import com.kintone.client.model.record.RecordNumberFieldValue;
import com.kintone.client.model.record.RichTextFieldValue;
import com.kintone.client.model.record.SingleLineTextFieldValue;
import com.kintone.client.model.record.StatusAssigneeFieldValue;
import com.kintone.client.model.record.StatusFieldValue;
import com.kintone.client.model.record.SubtableFieldValue;
import com.kintone.client.model.record.TableRow;
import com.kintone.client.model.record.TimeFieldValue;
import com.kintone.client.model.record.UpdatedTimeFieldValue;
import com.kintone.client.model.record.UserSelectFieldValue;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

class RecordDeserializer extends StdDeserializer<Record> {
    private static final long serialVersionUID = 603135607008372509L;
    private static final String RECORD_ID_FIELD_CODE = "$id";
    private static final String RECORD_REVISION_FIELD_CODE = "$revision";

    RecordDeserializer() {
        this(null);
    }

    RecordDeserializer(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public Record deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        Long recordId = parseFieldAsLong(node, RECORD_ID_FIELD_CODE);
        Long revision = parseFieldAsLong(node, RECORD_REVISION_FIELD_CODE);
        Record record = new Record(recordId, revision);
        for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> jsonField = it.next();

            String type = jsonField.getValue().get("type").textValue();
            JsonNode valueNode = jsonField.getValue().get("value");
            FieldType fieldType = FieldType.valueOf(type);
            if (fieldType != FieldType.__ID__ && fieldType != FieldType.__REVISION__) {
                String fieldCode = jsonField.getKey();
                FieldValue fieldValue = deserialize(fieldType, valueNode);
                record.putField(fieldCode, fieldValue);
            }
        }

        return record;
    }

    private FieldValue deserialize(FieldType fieldType, JsonNode node) {
        switch (fieldType) {
            case CALC:
                {
                    return new CalcFieldValue(node.textValue());
                }
            case CATEGORY:
                {
                    List<String> value = convertValues(node, JsonNode::textValue);
                    return new CategoryFieldValue(value);
                }
            case CHECK_BOX:
                {
                    List<String> value = convertValues(node, JsonNode::textValue);
                    return new CheckBoxFieldValue(value);
                }
            case CREATED_TIME:
                {
                    ZonedDateTime value = convertValue(node, ZonedDateTime::parse);
                    return new CreatedTimeFieldValue(value);
                }
            case CREATOR:
                {
                    User value = deserializeUser(node);
                    return new CreatorFieldValue(value);
                }
            case DATE:
                {
                    LocalDate value = convertValue(node, LocalDate::parse);
                    return new DateFieldValue(value);
                }
            case DATETIME:
                {
                    ZonedDateTime value = convertValue(node, ZonedDateTime::parse);
                    return new DateTimeFieldValue(value);
                }
            case DROP_DOWN:
                return new DropDownFieldValue(node.textValue());
            case FILE:
                {
                    List<FileBody> values = convertValues(node, this::deserializeFile);
                    return new FileFieldValue(values);
                }
            case GROUP_SELECT:
                {
                    List<Group> groups = convertValues(node, this::deserializeGroup);
                    return new GroupSelectFieldValue(groups);
                }
            case LINK:
                return new LinkFieldValue(node.textValue());
            case MODIFIER:
                {
                    User value = deserializeUser(node);
                    return new ModifierFieldValue(value);
                }
            case MULTI_LINE_TEXT:
                return new MultiLineTextFieldValue(node.textValue());
            case MULTI_SELECT:
                {
                    List<String> value = convertValues(node, JsonNode::textValue);
                    return new MultiSelectFieldValue(value);
                }
            case NUMBER:
                {
                    BigDecimal value = convertValue(node, BigDecimal::new);
                    return new NumberFieldValue(value);
                }
            case ORGANIZATION_SELECT:
                {
                    List<Organization> orgs = convertValues(node, this::deserializeOrganization);
                    return new OrganizationSelectFieldValue(orgs);
                }
            case RADIO_BUTTON:
                return new RadioButtonFieldValue(node.textValue());
            case RECORD_NUMBER:
                return new RecordNumberFieldValue(node.textValue());
            case RICH_TEXT:
                return new RichTextFieldValue(node.textValue());
            case SINGLE_LINE_TEXT:
                return new SingleLineTextFieldValue(node.textValue());
            case STATUS:
                return new StatusFieldValue(node.textValue());
            case STATUS_ASSIGNEE:
                {
                    List<User> value = convertValues(node, this::deserializeUser);
                    return new StatusAssigneeFieldValue(value);
                }
            case SUBTABLE:
                return parseSubtable(node);
            case TIME:
                {
                    LocalTime value = convertValue(node, LocalTime::parse);
                    return new TimeFieldValue(value);
                }
            case UPDATED_TIME:
                {
                    ZonedDateTime value = convertValue(node, ZonedDateTime::parse);
                    return new UpdatedTimeFieldValue(value);
                }
            case USER_SELECT:
                {
                    List<User> users = convertValues(node, this::deserializeUser);
                    return new UserSelectFieldValue(users);
                }
            default:
                throw new KintoneRuntimeException("Invalid field type: " + fieldType);
        }
    }

    private <T> T convertValue(JsonNode node, Function<String, T> func) {
        String value = node.textValue();
        if (value == null || value.isEmpty()) {
            return null;
        }
        return func.apply(value);
    }

    private <T> List<T> convertValues(JsonNode node, Function<JsonNode, T> func) {
        List<T> result = new ArrayList<>();
        for (Iterator<JsonNode> it = node.elements(); it.hasNext(); ) {
            JsonNode child = it.next();
            result.add(func.apply(child));
        }
        return result;
    }

    private SubtableFieldValue parseSubtable(JsonNode node) {
        List<TableRow> rows = new ArrayList<>();
        for (Iterator<JsonNode> it = node.elements(); it.hasNext(); ) {
            JsonNode recordField = it.next();
            TableRow row = parseTableRow(recordField);
            rows.add(row);
        }
        return new SubtableFieldValue(rows);
    }

    private TableRow parseTableRow(JsonNode recordNode) {
        Long id = Long.valueOf(recordNode.get("id").textValue());
        TableRow row = new TableRow(id);

        for (Iterator<Map.Entry<String, JsonNode>> it = recordNode.get("value").fields();
                it.hasNext(); ) {
            Map.Entry<String, JsonNode> child = it.next();
            String fieldCode = child.getKey();
            String type = child.getValue().get("type").textValue();
            JsonNode valueNode = child.getValue().get("value");
            FieldValue fieldValue = deserialize(FieldType.valueOf(type), valueNode);
            row.putField(fieldCode, fieldValue);
        }
        return row;
    }

    private Long parseFieldAsLong(JsonNode node, String fieldCode) {
        JsonNode fieldNode = node.get(fieldCode);
        if (fieldNode != null && fieldNode.get("value") != null) {
            return convertValue(fieldNode.get("value"), Long::valueOf);
        }
        return null;
    }

    private FileBody deserializeFile(JsonNode node) {
        FileBody body = new FileBody();
        body.setContentType(node.get("contentType").textValue());
        body.setFileKey(node.get("fileKey").textValue());
        body.setName(node.get("name").textValue());
        body.setSize(Integer.valueOf(node.get("size").textValue()));
        return body;
    }

    private User deserializeUser(JsonNode node) {
        String name = node.get("name").asText();
        String code = node.get("code").asText();
        return new User(name, code);
    }

    private Group deserializeGroup(JsonNode node) {
        String name = node.get("name").asText();
        String code = node.get("code").asText();
        return new Group(name, code);
    }

    private Organization deserializeOrganization(JsonNode node) {
        String name = node.get("name").asText();
        String code = node.get("code").asText();
        return new Organization(name, code);
    }
}
