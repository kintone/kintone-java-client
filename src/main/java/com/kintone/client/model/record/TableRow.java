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

/** A {@link TableRow} represents a row of Table field. */
public class TableRow {
    private final Map<String, FieldValue> fields = new HashMap<>();
    private final Long id;

    /**
     * Copies a row.
     *
     * @param src the source row
     * @return new row object
     */
    public static TableRow newFrom(TableRow src) {
        TableRow dst = new TableRow();
        dst.fields.putAll(src.fields);
        return dst;
    }

    /**
     * Constructor for creating or updating a row. This constructor does not set the ID of the
     * instance. {@link #getId()} will return null.
     */
    public TableRow() {
        this.id = null;
    }

    /**
     * Constructor to set the row ID. This constructor is mainly for internal use and for testing. To
     * get a new instance for creating or updating a row, simply use {@link #TableRow()}.
     *
     * @param id the row ID
     */
    public TableRow(Long id) {
        this.id = id;
    }

    /**
     * Returns the row ID.
     *
     * @return the row ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the value of a field.
     *
     * @param fieldCode the field code
     * @param value the value of the field
     * @return this row object
     */
    public TableRow putField(String fieldCode, FieldValue value) {
        if (value == null) {
            return removeField(fieldCode);
        }

        fields.put(fieldCode, value);
        return this;
    }

    /**
     * Remove a field from the row object.
     *
     * @param fieldCode the field code
     * @return this row object
     */
    public TableRow removeField(String fieldCode) {
        fields.remove(fieldCode);
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
     * Returns field codes stored in the row object.
     *
     * @return a set of field codes
     */
    public Set<String> getFieldCodes() {
        return new HashSet<>(fields.keySet());
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
    public BigDecimal getCalcFieldValue(String fieldCode) {
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
}
