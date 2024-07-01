package com.kintone.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kintone.client.exception.KintoneRuntimeException;
import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import com.kintone.client.model.app.field.Alignment;
import com.kintone.client.model.app.field.CalcFieldProperty;
import com.kintone.client.model.app.field.CategoryFieldProperty;
import com.kintone.client.model.app.field.CheckBoxFieldProperty;
import com.kintone.client.model.app.field.CreatedTimeFieldProperty;
import com.kintone.client.model.app.field.CreatorFieldProperty;
import com.kintone.client.model.app.field.DateFieldProperty;
import com.kintone.client.model.app.field.DateTimeFieldProperty;
import com.kintone.client.model.app.field.DisplayFormat;
import com.kintone.client.model.app.field.DropDownFieldProperty;
import com.kintone.client.model.app.field.FieldMapping;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.app.field.FileFieldProperty;
import com.kintone.client.model.app.field.GroupFieldProperty;
import com.kintone.client.model.app.field.GroupSelectFieldProperty;
import com.kintone.client.model.app.field.LinkFieldProperty;
import com.kintone.client.model.app.field.LinkProtocol;
import com.kintone.client.model.app.field.LookupFieldProperty;
import com.kintone.client.model.app.field.LookupSetting;
import com.kintone.client.model.app.field.ModifierFieldProperty;
import com.kintone.client.model.app.field.MultiLineTextFieldProperty;
import com.kintone.client.model.app.field.MultiSelectFieldProperty;
import com.kintone.client.model.app.field.NumberFieldProperty;
import com.kintone.client.model.app.field.Option;
import com.kintone.client.model.app.field.OrganizationSelectFieldProperty;
import com.kintone.client.model.app.field.RadioButtonFieldProperty;
import com.kintone.client.model.app.field.RecordNumberFieldProperty;
import com.kintone.client.model.app.field.ReferenceTable;
import com.kintone.client.model.app.field.ReferenceTableCondition;
import com.kintone.client.model.app.field.ReferenceTableFieldProperty;
import com.kintone.client.model.app.field.RelatedApp;
import com.kintone.client.model.app.field.RichTextFieldProperty;
import com.kintone.client.model.app.field.SingleLineTextFieldProperty;
import com.kintone.client.model.app.field.StatusAssigneeFieldProperty;
import com.kintone.client.model.app.field.StatusFieldProperty;
import com.kintone.client.model.app.field.SubtableFieldProperty;
import com.kintone.client.model.app.field.TimeFieldProperty;
import com.kintone.client.model.app.field.UnitPosition;
import com.kintone.client.model.app.field.UpdatedTimeFieldProperty;
import com.kintone.client.model.app.field.UserSelectFieldProperty;
import com.kintone.client.model.record.FieldType;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FieldPropertyDeserializerTest {
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(FieldProperty.class, new FieldPropertyDeserializer());
        mapper = new ObjectMapper();
        mapper.registerModule(module);
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void deserialize_CALC() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_CALC.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(CalcFieldProperty.class);

        CalcFieldProperty obj = (CalcFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.CALC);
        assertThat(obj.getCode()).isEqualTo("calc");
        assertThat(obj.getLabel()).isEqualTo("Calc field");
        assertThat(obj.getNoLabel()).isFalse();
        assertThat(obj.getRequired()).isFalse();
        assertThat(obj.getExpression()).isEqualTo("number + 100");
        assertThat(obj.getFormat()).isEqualTo(DisplayFormat.NUMBER);
        assertThat(obj.getDisplayScale()).isEqualTo(4L);
        assertThat(obj.getHideExpression()).isFalse();
        assertThat(obj.getUnit()).isEqualTo("$");
        assertThat(obj.getUnitPosition()).isEqualTo(UnitPosition.BEFORE);
    }

    @Test
    public void deserialize_CATEGORY() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_CATEGORY.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(CategoryFieldProperty.class);

        CategoryFieldProperty obj = (CategoryFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.CATEGORY);
        assertThat(obj.getCode()).isEqualTo("category");
        assertThat(obj.getLabel()).isEqualTo("Category field");
        assertThat(obj.getEnabled()).isTrue();
    }

    @Test
    public void deserialize_CHECK_BOX() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_CHECK_BOX.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(CheckBoxFieldProperty.class);

        CheckBoxFieldProperty obj = (CheckBoxFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.CHECK_BOX);
        assertThat(obj.getCode()).isEqualTo("checkbox");
        assertThat(obj.getLabel()).isEqualTo("Check box field");
        assertThat(obj.getNoLabel()).isFalse();
        assertThat(obj.getRequired()).isFalse();
        assertThat(obj.getDefaultValue()).isEmpty();
        assertThat(obj.getAlign()).isEqualTo(Alignment.HORIZONTAL);
        assertThat(obj.getOptions()).hasSize(3);
        assertThat(obj.getOptions().get("option 1"))
                .isEqualTo(new Option().setIndex(0L).setLabel("option 1"));
        assertThat(obj.getOptions().get("option 2"))
                .isEqualTo(new Option().setIndex(1L).setLabel("option 2"));
        assertThat(obj.getOptions().get("option 3"))
                .isEqualTo(new Option().setIndex(2L).setLabel("option 3"));
    }

    @Test
    public void deserialize_CREATED_TIME() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_CREATED_TIME.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(CreatedTimeFieldProperty.class);

        CreatedTimeFieldProperty obj = (CreatedTimeFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.CREATED_TIME);
        assertThat(obj.getCode()).isEqualTo("created_time");
        assertThat(obj.getLabel()).isEqualTo("Created datetime field");
        assertThat(obj.getNoLabel()).isFalse();
    }

    @Test
    public void deserialize_CREATOR() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_CREATOR.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(CreatorFieldProperty.class);

        CreatorFieldProperty obj = (CreatorFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.CREATOR);
        assertThat(obj.getCode()).isEqualTo("creator");
        assertThat(obj.getLabel()).isEqualTo("Creator field");
        assertThat(obj.getNoLabel()).isTrue();
    }

    @Test
    public void deserialize_DATE() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_DATE.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(DateFieldProperty.class);

        DateFieldProperty obj = (DateFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.DATE);
        assertThat(obj.getCode()).isEqualTo("date");
        assertThat(obj.getLabel()).isEqualTo("Date field");
        assertThat(obj.getNoLabel()).isTrue();
        assertThat(obj.getRequired()).isTrue();
        assertThat(obj.getUnique()).isTrue();
        assertThat(obj.getDefaultValue()).isEqualTo(LocalDate.of(2020, 11, 12));
        assertThat(obj.getDefaultNowValue()).isFalse();
    }

    @Test
    public void deserialize_DATETIME() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_DATETIME.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(DateTimeFieldProperty.class);

        DateTimeFieldProperty obj = (DateTimeFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.DATETIME);
        assertThat(obj.getCode()).isEqualTo("datetime");
        assertThat(obj.getLabel()).isEqualTo("Datetime field");
        assertThat(obj.getNoLabel()).isFalse();
        assertThat(obj.getRequired()).isFalse();
        assertThat(obj.getUnique()).isFalse();
        assertThat(obj.getDefaultValue()).isEqualTo(LocalDateTime.of(2020, 1, 1, 1, 30));
        assertThat(obj.getDefaultNowValue()).isFalse();
    }

    @Test
    public void deserialize_DROP_DOWN() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_DROP_DOWN.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(DropDownFieldProperty.class);

        DropDownFieldProperty obj = (DropDownFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.DROP_DOWN);
        assertThat(obj.getCode()).isEqualTo("dropdown");
        assertThat(obj.getLabel()).isEqualTo("Drop-down field");
        assertThat(obj.getNoLabel()).isFalse();
        assertThat(obj.getRequired()).isFalse();
        assertThat(obj.getDefaultValue()).isEqualTo("option 1");
        assertThat(obj.getOptions()).hasSize(3);
        assertThat(obj.getOptions().get("option 1"))
                .isEqualTo(new Option().setIndex(0L).setLabel("option 1"));
        assertThat(obj.getOptions().get("option 2"))
                .isEqualTo(new Option().setIndex(1L).setLabel("option 2"));
        assertThat(obj.getOptions().get("option 3"))
                .isEqualTo(new Option().setIndex(2L).setLabel("option 3"));
    }

    @Test
    public void deserialize_FILE() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_FILE.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(FileFieldProperty.class);

        FileFieldProperty obj = (FileFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.FILE);
        assertThat(obj.getCode()).isEqualTo("file");
        assertThat(obj.getLabel()).isEqualTo("File field");
        assertThat(obj.getNoLabel()).isTrue();
        assertThat(obj.getRequired()).isFalse();
        assertThat(obj.getThumbnailSize()).isEqualTo(500);
    }

    @Test
    public void deserialize_GROUP() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_GROUP.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(GroupFieldProperty.class);

        GroupFieldProperty obj = (GroupFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.GROUP);
        assertThat(obj.getCode()).isEqualTo("group");
        assertThat(obj.getLabel()).isEqualTo("Field group");
        assertThat(obj.getNoLabel()).isFalse();
        assertThat(obj.getOpenGroup()).isFalse();
    }

    @Test
    public void deserialize_GROUP_SELECT() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_GROUP_SELECT.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(GroupSelectFieldProperty.class);

        GroupSelectFieldProperty obj = (GroupSelectFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.GROUP_SELECT);
        assertThat(obj.getCode()).isEqualTo("groupselect");
        assertThat(obj.getLabel()).isEqualTo("Group selection field");
        assertThat(obj.getNoLabel()).isFalse();
        assertThat(obj.getRequired()).isFalse();
        assertThat(obj.getDefaultValue()).containsExactly(new Entity(EntityType.GROUP, "group"));
        assertThat(obj.getEntities()).containsExactly(new Entity(EntityType.GROUP, "group"));
    }

    @Test
    public void deserialize_HR() {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_HR.json");
        Throwable thrown = catchThrowable(() -> mapper.readValue(url, TestObject.class));
        assertThat(thrown.getCause())
                .isInstanceOf(KintoneRuntimeException.class)
                .hasMessage("Invalid field type: HR");
    }

    @Test
    public void deserialize_LABEL() {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_LABEL.json");
        Throwable thrown = catchThrowable(() -> mapper.readValue(url, TestObject.class));
        assertThat(thrown.getCause())
                .isInstanceOf(KintoneRuntimeException.class)
                .hasMessage("Invalid field type: LABEL");
    }

    @Test
    public void deserialize_LINK() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_LINK.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(LinkFieldProperty.class);

        LinkFieldProperty obj = (LinkFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.LINK);
        assertThat(obj.getCode()).isEqualTo("link");
        assertThat(obj.getLabel()).isEqualTo("Link field");
        assertThat(obj.getNoLabel()).isFalse();
        assertThat(obj.getRequired()).isFalse();
        assertThat(obj.getUnique()).isFalse();
        assertThat(obj.getDefaultValue()).isEqualTo("http://example.com/");
        assertThat(obj.getMaxLength()).isEqualTo(128);
        assertThat(obj.getMinLength()).isEqualTo(1);
        assertThat(obj.getProtocol()).isEqualTo(LinkProtocol.WEB);
    }

    @Test
    public void deserialize_MODIFIER() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_MODIFIER.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(ModifierFieldProperty.class);

        ModifierFieldProperty obj = (ModifierFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.MODIFIER);
        assertThat(obj.getCode()).isEqualTo("modifier");
        assertThat(obj.getLabel()).isEqualTo("Modifier field");
        assertThat(obj.getNoLabel()).isFalse();
    }

    @Test
    public void deserialize_MULTI_LINE_TEXT() throws IOException {
        URL url =
                getClass().getResource("FieldPropertyDeserializerTest_deserialize_MULTI_LINE_TEXT.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(MultiLineTextFieldProperty.class);

        MultiLineTextFieldProperty obj = (MultiLineTextFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.MULTI_LINE_TEXT);
        assertThat(obj.getCode()).isEqualTo("textarea");
        assertThat(obj.getLabel()).isEqualTo("Text area field");
        assertThat(obj.getNoLabel()).isFalse();
        assertThat(obj.getRequired()).isFalse();
        assertThat(obj.getDefaultValue()).isEqualTo("");
    }

    @Test
    public void deserialize_MULTI_SELECT() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_MULTI_SELECT.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(MultiSelectFieldProperty.class);

        MultiSelectFieldProperty obj = (MultiSelectFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.MULTI_SELECT);
        assertThat(obj.getCode()).isEqualTo("multichoice");
        assertThat(obj.getLabel()).isEqualTo("Multi-choice field");
        assertThat(obj.getNoLabel()).isFalse();
        assertThat(obj.getRequired()).isTrue();
        assertThat(obj.getDefaultValue()).containsExactly("option 2", "option 3");
        assertThat(obj.getOptions()).hasSize(3);
        assertThat(obj.getOptions().get("option 1"))
                .isEqualTo(new Option().setIndex(0L).setLabel("option 1"));
        assertThat(obj.getOptions().get("option 2"))
                .isEqualTo(new Option().setIndex(1L).setLabel("option 2"));
        assertThat(obj.getOptions().get("option 3"))
                .isEqualTo(new Option().setIndex(2L).setLabel("option 3"));
    }

    @Test
    public void deserialize_NUMBER() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_NUMBER.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(NumberFieldProperty.class);

        NumberFieldProperty obj = (NumberFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.NUMBER);
        assertThat(obj.getCode()).isEqualTo("number");
        assertThat(obj.getLabel()).isEqualTo("Number field");
        assertThat(obj.getNoLabel()).isTrue();
        assertThat(obj.getRequired()).isTrue();
        assertThat(obj.getUnique()).isTrue();
        assertThat(obj.getMaxValue()).isEqualTo(new BigDecimal(200));
        assertThat(obj.getMinValue()).isEqualTo(new BigDecimal(10));
        assertThat(obj.getDisplayScale()).isEqualTo(3L);
        assertThat(obj.getDigit()).isTrue();
        assertThat(obj.getUnit()).isEqualTo("points");
        assertThat(obj.getUnitPosition()).isEqualTo(UnitPosition.AFTER);
    }

    @Test
    public void deserialize_ORGANIZATION_SELECT() throws IOException {
        URL url =
                getClass()
                        .getResource("FieldPropertyDeserializerTest_deserialize_ORGANIZATION_SELECT.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(OrganizationSelectFieldProperty.class);

        OrganizationSelectFieldProperty obj = (OrganizationSelectFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.ORGANIZATION_SELECT);
        assertThat(obj.getCode()).isEqualTo("orgselect");
        assertThat(obj.getLabel()).isEqualTo("Department selection field");
        assertThat(obj.getNoLabel()).isTrue();
        assertThat(obj.getRequired()).isTrue();
        assertThat(obj.getDefaultValue()).hasSize(2);
        assertThat(obj.getDefaultValue().get(0)).isEqualTo(new Entity(EntityType.ORGANIZATION, "org"));
        assertThat(obj.getDefaultValue().get(1))
                .isEqualTo(new Entity(EntityType.FUNCTION, "PRIMARY_ORGANIZATION()"));
        assertThat(obj.getEntities()).isEmpty();
    }

    @Test
    public void deserialize_RADIO_BUTTON() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_RADIO_BUTTON.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(RadioButtonFieldProperty.class);

        RadioButtonFieldProperty obj = (RadioButtonFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.RADIO_BUTTON);
        assertThat(obj.getCode()).isEqualTo("radiobutton");
        assertThat(obj.getLabel()).isEqualTo("Radio button field");
        assertThat(obj.getNoLabel()).isTrue();
        assertThat(obj.getRequired()).isTrue();
        assertThat(obj.getDefaultValue()).isEqualTo("option 3");
        assertThat(obj.getAlign()).isEqualTo(Alignment.VERTICAL);
        assertThat(obj.getOptions()).hasSize(3);
        assertThat(obj.getOptions().get("option 1"))
                .isEqualTo(new Option().setIndex(0L).setLabel("option 1"));
        assertThat(obj.getOptions().get("option 2"))
                .isEqualTo(new Option().setIndex(1L).setLabel("option 2"));
        assertThat(obj.getOptions().get("option 3"))
                .isEqualTo(new Option().setIndex(2L).setLabel("option 3"));
    }

    @Test
    public void deserialize_RECORD_NUMBER() throws IOException {
        URL url =
                getClass().getResource("FieldPropertyDeserializerTest_deserialize_RECORD_NUMBER.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(RecordNumberFieldProperty.class);

        RecordNumberFieldProperty obj = (RecordNumberFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.RECORD_NUMBER);
        assertThat(obj.getCode()).isEqualTo("record_number");
        assertThat(obj.getLabel()).isEqualTo("Record number field");
        assertThat(obj.getNoLabel()).isFalse();
    }

    @Test
    public void deserialize_REFERENCE_TABLE() throws IOException {
        URL url =
                getClass().getResource("FieldPropertyDeserializerTest_deserialize_REFERENCE_TABLE.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(ReferenceTableFieldProperty.class);

        ReferenceTableFieldProperty obj = (ReferenceTableFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.REFERENCE_TABLE);
        assertThat(obj.getCode()).isEqualTo("reference_table");
        assertThat(obj.getLabel()).isEqualTo("Related records field");
        assertThat(obj.getNoLabel()).isFalse();
        ReferenceTable setting = obj.getReferenceTable();
        assertThat(setting.getRelatedApp()).isEqualTo(new RelatedApp().setApp(1L).setCode(""));
        assertThat(setting.getCondition())
                .isEqualTo(
                        new ReferenceTableCondition().setField("Record_number").setRelatedField("Number"));
        assertThat(setting.getFilterCond()).isEqualTo("Number >= \"100\"");
        assertThat(setting.getDisplayFields()).containsExactly("Record_number", "Number");
        assertThat(setting.getSort()).isEqualTo("Record_number desc");
        assertThat(setting.getSize()).isEqualTo(5L);
    }

    @Test
    public void deserialize_RICH_TEXT() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_RICH_TEXT.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(RichTextFieldProperty.class);

        RichTextFieldProperty obj = (RichTextFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.RICH_TEXT);
        assertThat(obj.getCode()).isEqualTo("richtext");
        assertThat(obj.getLabel()).isEqualTo("Rich text field");
        assertThat(obj.getNoLabel()).isFalse();
        assertThat(obj.getRequired()).isFalse();
        assertThat(obj.getDefaultValue()).isEqualTo("<div><b>test value</b></div>");
    }

    @Test
    public void deserialize_SINGLE_LINE_TEXT() throws IOException {
        URL url =
                getClass().getResource("FieldPropertyDeserializerTest_deserialize_SINGLE_LINE_TEXT.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(SingleLineTextFieldProperty.class);

        SingleLineTextFieldProperty obj = (SingleLineTextFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.SINGLE_LINE_TEXT);
        assertThat(obj.getCode()).isEqualTo("text");
        assertThat(obj.getLabel()).isEqualTo("Text field");
        assertThat(obj.getNoLabel()).isFalse();
        assertThat(obj.getRequired()).isFalse();
        assertThat(obj.getUnique()).isFalse();
        assertThat(obj.getMaxLength()).isEqualTo(10L);
        assertThat(obj.getMinLength()).isEqualTo(0L);
        assertThat(obj.getDefaultValue()).isEqualTo("test");
        assertThat(obj.getExpression()).isEqualTo("");
        assertThat(obj.getHideExpression()).isFalse();
    }

    @Test
    public void deserialize_SPACER() {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_SPACER.json");
        Throwable thrown = catchThrowable(() -> mapper.readValue(url, TestObject.class));
        assertThat(thrown.getCause())
                .isInstanceOf(KintoneRuntimeException.class)
                .hasMessage("Invalid field type: SPACER");
    }

    @Test
    public void deserialize_STATUS() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_STATUS.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(StatusFieldProperty.class);

        StatusFieldProperty obj = (StatusFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.STATUS);
        assertThat(obj.getCode()).isEqualTo("status");
        assertThat(obj.getLabel()).isEqualTo("Status field");
        assertThat(obj.getEnabled()).isTrue();
    }

    @Test
    public void deserialize_STATUS_ASSIGNEE() throws IOException {
        URL url =
                getClass().getResource("FieldPropertyDeserializerTest_deserialize_STATUS_ASSIGNEE.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(StatusAssigneeFieldProperty.class);

        StatusAssigneeFieldProperty obj = (StatusAssigneeFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.STATUS_ASSIGNEE);
        assertThat(obj.getCode()).isEqualTo("assignee");
        assertThat(obj.getLabel()).isEqualTo("Assignee field");
        assertThat(obj.getEnabled()).isTrue();
    }

    @Test
    public void deserialize_SUBTABLE() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_SUBTABLE.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(SubtableFieldProperty.class);

        SubtableFieldProperty obj = (SubtableFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.SUBTABLE);
        assertThat(obj.getCode()).isEqualTo("table");
        assertThat(obj.getLabel()).isEqualTo("Subtable field");
        assertThat(obj.getNoLabel()).isFalse();
        assertThat(obj.getFields()).hasSize(2);

        SingleLineTextFieldProperty obj1 = (SingleLineTextFieldProperty) obj.getFields().get("text");
        assertThat(obj1.getType()).isEqualTo(FieldType.SINGLE_LINE_TEXT);
        assertThat(obj1.getCode()).isEqualTo("text");
        assertThat(obj1.getLabel()).isEqualTo("Text field");
        assertThat(obj1.getNoLabel()).isFalse();
        assertThat(obj1.getRequired()).isFalse();
        assertThat(obj1.getUnique()).isFalse();
        assertThat(obj1.getDefaultValue()).isEqualTo("");
        assertThat(obj1.getMaxLength()).isNull();
        assertThat(obj1.getMinLength()).isNull();
        assertThat(obj1.getExpression()).isEqualTo("");
        assertThat(obj1.getHideExpression()).isFalse();

        NumberFieldProperty obj2 = (NumberFieldProperty) obj.getFields().get("number");
        assertThat(obj2.getType()).isEqualTo(FieldType.NUMBER);
        assertThat(obj2.getCode()).isEqualTo("number");
        assertThat(obj2.getLabel()).isEqualTo("Number field");
        assertThat(obj2.getNoLabel()).isFalse();
        assertThat(obj2.getRequired()).isFalse();
        assertThat(obj2.getUnique()).isFalse();
        assertThat(obj2.getMaxValue()).isNull();
        assertThat(obj2.getMinValue()).isNull();
        assertThat(obj2.getDisplayScale()).isNull();
        assertThat(obj2.getDigit()).isFalse();
        assertThat(obj2.getUnit()).isEqualTo("");
        assertThat(obj2.getUnitPosition()).isEqualTo(UnitPosition.BEFORE);
    }

    @Test
    public void deserialize_TIME() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_TIME.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(TimeFieldProperty.class);

        TimeFieldProperty obj = (TimeFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.TIME);
        assertThat(obj.getCode()).isEqualTo("time");
        assertThat(obj.getLabel()).isEqualTo("Time field");
        assertThat(obj.getNoLabel()).isFalse();
        assertThat(obj.getRequired()).isTrue();
        assertThat(obj.getDefaultValue()).isEqualTo(LocalTime.of(5, 0));
        assertThat(obj.getDefaultNowValue()).isFalse();
    }

    @Test
    public void deserialize_TIME_defaultValueEmpty() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_TIME_defaultValueEmpty.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(TimeFieldProperty.class);

        TimeFieldProperty obj = (TimeFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.TIME);
        assertThat(obj.getCode()).isEqualTo("time");
        assertThat(obj.getLabel()).isEqualTo("Time field");
        assertThat(obj.getNoLabel()).isFalse();
        assertThat(obj.getRequired()).isFalse();
        assertThat(obj.getDefaultValue()).isNull();
        assertThat(obj.getDefaultNowValue()).isTrue();
    }

    @Test
    public void deserialize_UPDATED_TIME() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_UPDATED_TIME.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(UpdatedTimeFieldProperty.class);

        UpdatedTimeFieldProperty obj = (UpdatedTimeFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.UPDATED_TIME);
        assertThat(obj.getCode()).isEqualTo("updated_time");
        assertThat(obj.getLabel()).isEqualTo("Updated datetime field");
        assertThat(obj.getNoLabel()).isTrue();
    }

    @Test
    public void deserialize_USER_SELECT() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_USER_SELECT.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(UserSelectFieldProperty.class);

        UserSelectFieldProperty obj = (UserSelectFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.USER_SELECT);
        assertThat(obj.getCode()).isEqualTo("userselect");
        assertThat(obj.getLabel()).isEqualTo("User selection field");
        assertThat(obj.getNoLabel()).isFalse();
        assertThat(obj.getRequired()).isFalse();
        assertThat(obj.getDefaultValue()).isEmpty();
        assertThat(obj.getEntities()).hasSize(3);
        assertThat(obj.getEntities().get(0)).isEqualTo(new Entity(EntityType.ORGANIZATION, "org"));
        assertThat(obj.getEntities().get(1)).isEqualTo(new Entity(EntityType.GROUP, "group"));
        assertThat(obj.getEntities().get(2)).isEqualTo(new Entity(EntityType.USER, "user"));
    }

    @Test
    public void deserialize_USER_SELECT_FUNCTION() throws IOException {
        URL url =
                getClass()
                        .getResource("FieldPropertyDeserializerTest_deserialize_USER_SELECT_FUNCTION.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(UserSelectFieldProperty.class);

        UserSelectFieldProperty obj = (UserSelectFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.USER_SELECT);
        assertThat(obj.getCode()).isEqualTo("userselect");
        assertThat(obj.getLabel()).isEqualTo("User selection field");
        assertThat(obj.getNoLabel()).isFalse();
        assertThat(obj.getRequired()).isFalse();
        assertThat(obj.getDefaultValue()).hasSize(1);
        assertThat(obj.getDefaultValue().get(0))
                .isEqualTo(new Entity(EntityType.FUNCTION, "LOGINUSER()"));
        assertThat(obj.getEntities()).isEmpty();
    }

    @Test
    public void deserialize_lookup() throws IOException {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_lookup.json");

        TestObject result = mapper.readValue(url, TestObject.class);
        assertThat(result.getProperty()).isInstanceOf(LookupFieldProperty.class);

        LookupFieldProperty obj = (LookupFieldProperty) result.getProperty();
        assertThat(obj.getType()).isEqualTo(FieldType.NUMBER);
        assertThat(obj.getCode()).isEqualTo("lookup");
        assertThat(obj.getLabel()).isEqualTo("Lookup number field");
        assertThat(obj.getNoLabel()).isFalse();
        assertThat(obj.getRequired()).isFalse();

        LookupSetting setting = obj.getLookup();
        assertThat(setting.getRelatedApp()).isEqualTo(new RelatedApp().setApp(1L).setCode(""));
        assertThat(setting.getRelatedKeyField()).isEqualTo("record_number");
        assertThat(setting.getFieldMappings())
                .containsExactly(new FieldMapping().setField("number").setRelatedField("other_number"));
        assertThat(setting.getLookupPickerFields()).containsExactly("record_number", "other_number");
        assertThat(setting.getFilterCond()).isEqualTo("record_number >= \"10\"");
        assertThat(setting.getSort()).isEqualTo("record_number desc");
    }

    @Test
    public void deserialize_invalidType() {
        URL url = getClass().getResource("FieldPropertyDeserializerTest_deserialize_invalidType.json");
        Throwable thrown = catchThrowable(() -> mapper.readValue(url, TestObject.class));
        assertThat(thrown.getCause())
                .isInstanceOf(KintoneRuntimeException.class)
                .hasMessage("Invalid field type: TEST");
    }

    public static class TestObject {
        private FieldProperty property;

        public FieldProperty getProperty() {
            return property;
        }

        public void setProperty(FieldProperty property) {
            this.property = property;
        }
    }
}
