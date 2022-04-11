package com.kintone.client.model.record;

import com.kintone.client.model.FileBody;
import com.kintone.client.model.Group;
import com.kintone.client.model.Organization;
import com.kintone.client.model.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A {@link Record} represents a Record data.
 *
 * <h3>Retrieved Records</h3>
 *
 * This class is used as the responded record objects of GET Record API and GET Records API.
 *
 * <p>A value of any field in a record can be obtained by {@link #getFieldValue(String)} with its
 * field code, except for "$id" and "$revision" fields. These fields are decoded specially into
 * dedicated data and can be obtained by {@link #getId()} and {@link #getRevision()} each.
 *
 * <p>{@link #getFieldValue(String)} returns implementation classes of {@link FieldValue}, which
 * wrap the actual field value of {@code String}, {@code BigDecimal} and etc into a class depending
 * on the field type. For the convenience, {@code get{FIELD_TYPE}FieldValue(String)} methods, such
 * as {@link #getSingleLineTextFieldValue(String)}, allow you to directly get unwrapped values.
 *
 * <p>With GET Records API, retrieved records might not contain all the fields of the App. This REST
 * API returns the fields only specified in "fields" request parameter if it specified. Therefore,
 * the corresponding {@code Record} objects also has only the specified fields. If "$id" and
 * "$revision" is not specified in "fields" parameter, {@link #getId()} and {@link #getRevision()}
 * will be return null, respectively.
 *
 * <pre>{@code
 * List<Record> records = client.record().getRecords(app, Arrays.asList("field1"), 1, 0);
 * records.get(0).getFieldValue("field1"); // returns the value of "field1"
 * records.get(0).getId();                 // will be null, the response only contains the value of "field1"
 * records.get(0).getFieldValue("field2"); // also, null
 * }</pre>
 *
 * <h3>Modifying Records</h3>
 *
 * To add or update records, setup record objects with {@link #Record()} and {@link
 * #putField(String, FieldValue)}.
 *
 * <pre>{@code
 * // Creates a record object
 * Record record = new Record();
 *
 * // "number" and "text" are the field codes of the Number and Text field, respectively.
 * record.putField("number", new NumberFieldValue(30));
 * record.putField("text", new SingleLineTextFieldValue("Hello"));
 * }</pre>
 *
 * You can also create the record object from a retrieved record. In such a case, you may use {@link
 * #newFrom(Record)} to create the object.
 *
 * <pre>{@code
 * // Gets a record
 * Record record = client.record().getRecord(1, 100);
 *
 * // Creates new Record object from the retrieved record,
 * // copying without built-in fields, namely Record Number and Created date and time.
 * Record copiedRecord = Record.newFrom(record);
 *
 * // Adds the record
 * long recordId = client.record().addRecord(1, copiedRecord);
 * }</pre>
 *
 * Retrieved records may contain some built-in fields that will cause errors when sent them as is to
 * add or update records. {@link #newFrom(Record)} create a new record object from the source record
 * without built-in fields to prevent those errors.
 *
 * <p>Note that Kintone only allows to set the value of some built-in field (specifically, Creator,
 * Modifier, and Created or Updated date-time field) when adding a new record as an administrator of
 * the App.
 */
public class Record {
    private final Map<String, FieldValue> fields = new HashMap<>();
    private final Map<FieldType, String> builtinCodes = new HashMap<>();
    private final Long id;
    private final Long revision;

    /**
     * Copies a record without built-in fields.
     *
     * @param src the source record
     * @return new record object with non built-in fields of {@code src}
     */
    public static Record newFrom(Record src) {
        Record dst = new Record();
        for (Map.Entry<String, FieldValue> entry : src.fields.entrySet()) {
            if (!entry.getValue().isBuiltin()) {
                dst.putField(entry.getKey(), entry.getValue());
            }
        }
        return dst;
    }

    /**
     * Constructor for creating or updating a record. This constructor does not set the ID and
     * revision data of creating instance. {@link #getId()} and {@link #getRevision()} will return
     * null.
     */
    public Record() {
        this.id = null;
        this.revision = null;
    }

    /**
     * Constructor to set the ID and revision data. This constructor is mainly for internal use and
     * for testing. To get a new instance for creating or updating a record, simply use {@link
     * #Record()}.
     *
     * @param id the record ID
     * @param revision the revision of the record
     */
    public Record(Long id, Long revision) {
        this.id = id;
        this.revision = revision;
    }

    /**
     * Returns the record ID.
     *
     * @return the record ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the record revision.
     *
     * @return the record revision
     */
    public Long getRevision() {
        return revision;
    }

    /**
     * Set the value of a field.
     *
     * @param fieldCode the field code
     * @param value the value of the field
     * @return this record object
     */
    public Record putField(String fieldCode, FieldValue value) {
        if (value == null) {
            return removeField(fieldCode);
        }

        if (value.isBuiltin()) {
            String prevCode = builtinCodes.put(value.getType(), fieldCode);
            if (prevCode != null) {
                fields.remove(prevCode);
            }
        }

        fields.put(fieldCode, value);
        return this;
    }

    /**
     * Remove a field from the record object.
     *
     * @param fieldCode the field code
     * @return this record object
     */
    public Record removeField(String fieldCode) {
        FieldValue removed = fields.remove(fieldCode);
        if (removed != null && removed.isBuiltin()) {
            builtinCodes.remove(removed.getType());
        }
        return this;
    }

    /**
     * Returns the value of the specified field.
     *
     * @param fieldCode the field code
     * @return field value
     */
    public FieldValue getFieldValue(String fieldCode) {
        return fields.get(fieldCode);
    }

    /**
     * Returns field codes stored in the record object.
     *
     * @param includeBuiltins true to include field code of built-in fields, false to get only non
     *     built-in fields. "$id" and "$revision" are not included in either case.
     * @return a set of field codes
     */
    public Set<String> getFieldCodes(boolean includeBuiltins) {
        Set<String> fieldCodes = new HashSet<>(fields.keySet());
        if (!includeBuiltins) {
            fieldCodes.removeAll(builtinCodes.values());
        }
        return fieldCodes;
    }

    /**
     * Returns the field type of the field.
     *
     * @param fieldCode the field code
     * @return the type of the field
     */
    public FieldType getFieldType(String fieldCode) {
        FieldValue value = fields.get(fieldCode);
        return value == null ? null : value.getType();
    }

    /**
     * Returns the value of a Text field.
     *
     * @param fieldCode the field code
     * @return the value of the field
     */
    public String getSingleLineTextFieldValue(String fieldCode) {
        SingleLineTextFieldValue value = (SingleLineTextFieldValue) fields.get(fieldCode);
        return value == null ? null : value.getValue();
    }

    /**
     * Returns the value of a Table.
     *
     * @param fieldCode the field code
     * @return the value of the field
     */
    public List<TableRow> getSubtableFieldValue(String fieldCode) {
        SubtableFieldValue value = (SubtableFieldValue) fields.get(fieldCode);
        return value == null ? null : value.getRows();
    }

    /**
     * Returns the value of a Link field.
     *
     * @param fieldCode the field code
     * @return the value of the field
     */
    public String getLinkFieldValue(String fieldCode) {
        LinkFieldValue value = (LinkFieldValue) fields.get(fieldCode);
        return value == null ? null : value.getValue();
    }

    /**
     * Returns the value of a Group Selection field.
     *
     * @param fieldCode the field code
     * @return the value of the field
     */
    public List<Group> getGroupSelectFieldValue(String fieldCode) {
        GroupSelectFieldValue value = (GroupSelectFieldValue) fields.get(fieldCode);
        return value == null ? null : value.getValues();
    }

    /**
     * Returns the value of a Multi-choice field.
     *
     * @param fieldCode the field code
     * @return the value of the field
     */
    public List<String> getMultiSelectFieldValue(String fieldCode) {
        MultiSelectFieldValue value = (MultiSelectFieldValue) fields.get(fieldCode);
        return value == null ? null : value.getValues();
    }

    /**
     * Returns the value of a Radio button field.
     *
     * @param fieldCode the field code
     * @return the value of the field
     */
    public String getRadioButtonFieldValue(String fieldCode) {
        RadioButtonFieldValue value = (RadioButtonFieldValue) fields.get(fieldCode);
        return value == null ? null : value.getValue();
    }

    /**
     * Returns the value of a Date field.
     *
     * @param fieldCode the field code
     * @return the value of the field
     */
    public LocalDate getDateFieldValue(String fieldCode) {
        DateFieldValue value = (DateFieldValue) fields.get(fieldCode);
        return value == null ? null : value.getValue();
    }

    /**
     * Returns the value of a Department Selection field.
     *
     * @param fieldCode the field code
     * @return the value of the field
     */
    public List<Organization> getOrganizationSelectFieldValue(String fieldCode) {
        OrganizationSelectFieldValue value = (OrganizationSelectFieldValue) fields.get(fieldCode);
        return value == null ? null : value.getValues();
    }

    /**
     * Returns the value of a Date and time field.
     *
     * @param fieldCode the field code
     * @return the value of the field
     */
    public ZonedDateTime getDateTimeFieldValue(String fieldCode) {
        DateTimeFieldValue value = (DateTimeFieldValue) fields.get(fieldCode);
        return value == null ? null : value.getValue();
    }

    /**
     * Returns the value of a User Selection field.
     *
     * @param fieldCode the field code
     * @return the value of the field
     */
    public List<User> getUserSelectFieldValue(String fieldCode) {
        UserSelectFieldValue value = (UserSelectFieldValue) fields.get(fieldCode);
        return value == null ? null : value.getValues();
    }

    /**
     * Returns the value of a Checkbox field.
     *
     * @param fieldCode the field code
     * @return the value of the field
     */
    public List<String> getCheckBoxFieldValue(String fieldCode) {
        CheckBoxFieldValue value = (CheckBoxFieldValue) fields.get(fieldCode);
        return value == null ? null : value.getValues();
    }

    /**
     * Returns the value of a Calculated field.
     *
     * @param fieldCode the field code
     * @return the value of the field
     */
    public String getCalcFieldValue(String fieldCode) {
        CalcFieldValue value = (CalcFieldValue) fields.get(fieldCode);
        return value == null ? null : value.getValue();
    }

    /**
     * Returns the value of a Time field.
     *
     * @param fieldCode the field code
     * @return the value of the field
     */
    public LocalTime getTimeFieldValue(String fieldCode) {
        TimeFieldValue value = (TimeFieldValue) fields.get(fieldCode);
        return value == null ? null : value.getValue();
    }

    /**
     * Returns the value of a Number field.
     *
     * @param fieldCode the field code
     * @return the value of the field
     */
    public BigDecimal getNumberFieldValue(String fieldCode) {
        NumberFieldValue value = (NumberFieldValue) fields.get(fieldCode);
        return value == null ? null : value.getValue();
    }

    /**
     * Returns the value of a Drop-down field.
     *
     * @param fieldCode the field code
     * @return the value of the field
     */
    public String getDropDownFieldValue(String fieldCode) {
        DropDownFieldValue value = (DropDownFieldValue) fields.get(fieldCode);
        return value == null ? null : value.getValue();
    }

    /**
     * Returns the value of a Text Area field.
     *
     * @param fieldCode the field code
     * @return the value of the field
     */
    public String getMultiLineTextFieldValue(String fieldCode) {
        MultiLineTextFieldValue value = (MultiLineTextFieldValue) fields.get(fieldCode);
        return value == null ? null : value.getValue();
    }

    /**
     * Returns the value of a Rich text field.
     *
     * @param fieldCode the field code
     * @return the value of the field
     */
    public String getRichTextFieldValue(String fieldCode) {
        RichTextFieldValue value = (RichTextFieldValue) fields.get(fieldCode);
        return value == null ? null : value.getValue();
    }

    /**
     * Returns the value of an Attachment field.
     *
     * @param fieldCode the field code
     * @return the value of the field
     */
    public List<FileBody> getFileFieldValue(String fieldCode) {
        FileFieldValue value = (FileFieldValue) fields.get(fieldCode);
        return value == null ? null : value.getValues();
    }

    /**
     * Returns the value of the Category field.
     *
     * @return the value of the field
     */
    public List<String> getCategoryFieldValue() {
        CategoryFieldValue value = (CategoryFieldValue) getBuiltinFieldValue(FieldType.CATEGORY);
        return value == null ? null : value.getValues();
    }

    /**
     * Returns the value of the Created date-time field.
     *
     * @return the value of the field
     */
    public ZonedDateTime getCreatedTimeFieldValue() {
        CreatedTimeFieldValue value =
                (CreatedTimeFieldValue) getBuiltinFieldValue(FieldType.CREATED_TIME);
        return value == null ? null : value.getValue();
    }

    /**
     * Returns the value of the Creator field.
     *
     * @return the value of the field
     */
    public User getCreatorFieldValue() {
        CreatorFieldValue value = (CreatorFieldValue) getBuiltinFieldValue(FieldType.CREATOR);
        return value == null ? null : value.getValue();
    }

    /**
     * Returns the value of the Modifier field.
     *
     * @return the value of the field
     */
    public User getModifierFieldValue() {
        ModifierFieldValue value = (ModifierFieldValue) getBuiltinFieldValue(FieldType.MODIFIER);
        return value == null ? null : value.getValue();
    }

    /**
     * Returns the value of the Record number field.
     *
     * @return the value of the field
     */
    public String getRecordNumberFieldValue() {
        RecordNumberFieldValue value =
                (RecordNumberFieldValue) getBuiltinFieldValue(FieldType.RECORD_NUMBER);
        return value == null ? null : value.getValue();
    }

    /**
     * Returns the value of the Process management status field.
     *
     * @return the value of the field
     */
    public String getStatusFieldValue() {
        StatusFieldValue value = (StatusFieldValue) getBuiltinFieldValue(FieldType.STATUS);
        return value == null ? null : value.getValue();
    }

    /**
     * Returns the value of the Assignee of the Process management status field.
     *
     * @return the value of the field
     */
    public List<User> getStatusAssigneeFieldValue() {
        StatusAssigneeFieldValue value =
                (StatusAssigneeFieldValue) getBuiltinFieldValue(FieldType.STATUS_ASSIGNEE);
        return value == null ? null : value.getValues();
    }

    /**
     * Returns the value of the Updated date-time field.
     *
     * @return the value of the field
     */
    public ZonedDateTime getUpdatedTimeFieldValue() {
        UpdatedTimeFieldValue value =
                (UpdatedTimeFieldValue) getBuiltinFieldValue(FieldType.UPDATED_TIME);
        return value == null ? null : value.getValue();
    }

    private FieldValue getBuiltinFieldValue(FieldType type) {
        String fieldCode = builtinCodes.get(type);
        return fieldCode == null ? null : fields.get(fieldCode);
    }
}
