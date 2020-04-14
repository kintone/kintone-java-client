package com.kintone.client.model.record;

import com.kintone.client.model.app.field.CalcFieldProperty;
import com.kintone.client.model.app.field.CategoryFieldProperty;
import com.kintone.client.model.app.field.CheckBoxFieldProperty;
import com.kintone.client.model.app.field.CreatedTimeFieldProperty;
import com.kintone.client.model.app.field.CreatorFieldProperty;
import com.kintone.client.model.app.field.DateFieldProperty;
import com.kintone.client.model.app.field.DateTimeFieldProperty;
import com.kintone.client.model.app.field.DropDownFieldProperty;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.app.field.FileFieldProperty;
import com.kintone.client.model.app.field.GroupFieldProperty;
import com.kintone.client.model.app.field.GroupSelectFieldProperty;
import com.kintone.client.model.app.field.LinkFieldProperty;
import com.kintone.client.model.app.field.ModifierFieldProperty;
import com.kintone.client.model.app.field.MultiLineTextFieldProperty;
import com.kintone.client.model.app.field.MultiSelectFieldProperty;
import com.kintone.client.model.app.field.NumberFieldProperty;
import com.kintone.client.model.app.field.OrganizationSelectFieldProperty;
import com.kintone.client.model.app.field.RadioButtonFieldProperty;
import com.kintone.client.model.app.field.RecordNumberFieldProperty;
import com.kintone.client.model.app.field.ReferenceTableFieldProperty;
import com.kintone.client.model.app.field.RichTextFieldProperty;
import com.kintone.client.model.app.field.SingleLineTextFieldProperty;
import com.kintone.client.model.app.field.StatusAssigneeFieldProperty;
import com.kintone.client.model.app.field.StatusFieldProperty;
import com.kintone.client.model.app.field.SubtableFieldProperty;
import com.kintone.client.model.app.field.TimeFieldProperty;
import com.kintone.client.model.app.field.UpdatedTimeFieldProperty;
import com.kintone.client.model.app.field.UserSelectFieldProperty;
import lombok.Getter;

/** The type of field. */
public enum FieldType {
    CALC(false, CalcFieldValue.class, CalcFieldProperty.class),
    CATEGORY(true, CategoryFieldValue.class, CategoryFieldProperty.class),
    CHECK_BOX(false, CheckBoxFieldValue.class, CheckBoxFieldProperty.class),
    CREATED_TIME(true, CreatedTimeFieldValue.class, CreatedTimeFieldProperty.class),
    CREATOR(true, CreatorFieldValue.class, CreatorFieldProperty.class),
    DATE(false, DateFieldValue.class, DateFieldProperty.class),
    DATETIME(false, DateTimeFieldValue.class, DateTimeFieldProperty.class),
    DROP_DOWN(false, DropDownFieldValue.class, DropDownFieldProperty.class),
    FILE(false, FileFieldValue.class, FileFieldProperty.class),
    GROUP(false, null, GroupFieldProperty.class),
    GROUP_SELECT(false, GroupSelectFieldValue.class, GroupSelectFieldProperty.class),
    HR(false, null, null),
    LABEL(false, null, null),
    LINK(false, LinkFieldValue.class, LinkFieldProperty.class),
    MODIFIER(true, ModifierFieldValue.class, ModifierFieldProperty.class),
    MULTI_LINE_TEXT(false, MultiLineTextFieldValue.class, MultiLineTextFieldProperty.class),
    MULTI_SELECT(false, MultiSelectFieldValue.class, MultiSelectFieldProperty.class),
    NUMBER(false, NumberFieldValue.class, NumberFieldProperty.class),
    ORGANIZATION_SELECT(
            false, OrganizationSelectFieldValue.class, OrganizationSelectFieldProperty.class),
    RADIO_BUTTON(false, RadioButtonFieldValue.class, RadioButtonFieldProperty.class),
    RECORD_NUMBER(true, RecordNumberFieldValue.class, RecordNumberFieldProperty.class),
    REFERENCE_TABLE(false, null, ReferenceTableFieldProperty.class),
    RICH_TEXT(false, RichTextFieldValue.class, RichTextFieldProperty.class),
    SINGLE_LINE_TEXT(false, SingleLineTextFieldValue.class, SingleLineTextFieldProperty.class),
    SPACER(false, null, null),
    STATUS(true, StatusFieldValue.class, StatusFieldProperty.class),
    STATUS_ASSIGNEE(true, StatusAssigneeFieldValue.class, StatusAssigneeFieldProperty.class),
    SUBTABLE(false, SubtableFieldValue.class, SubtableFieldProperty.class),
    TIME(false, TimeFieldValue.class, TimeFieldProperty.class),
    UPDATED_TIME(true, UpdatedTimeFieldValue.class, UpdatedTimeFieldProperty.class),
    USER_SELECT(false, UserSelectFieldValue.class, UserSelectFieldProperty.class),
    __ID__(true, null, null),
    __REVISION__(true, null, null);

    @Getter private final Class<? extends FieldValue> fieldValueClass;

    @Getter private final Class<? extends FieldProperty> fieldPropertyClass;

    @Getter private final boolean isBuiltin;

    FieldType(
            boolean isBuiltin,
            Class<? extends FieldValue> fieldValueClass,
            Class<? extends FieldProperty> fieldPropertyClass) {
        this.isBuiltin = isBuiltin;
        this.fieldValueClass = fieldValueClass;
        this.fieldPropertyClass = fieldPropertyClass;
    }
}
