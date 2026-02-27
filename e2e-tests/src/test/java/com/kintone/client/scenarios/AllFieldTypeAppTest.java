package com.kintone.client.scenarios;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.api.app.AddAppRequest;
import com.kintone.client.api.app.AddAppResponseBody;
import com.kintone.client.api.app.DeployAppRequest;
import com.kintone.client.api.app.GetDeployStatusRequest;
import com.kintone.client.api.app.GetDeployStatusResponseBody;
import com.kintone.client.model.*;
import com.kintone.client.model.app.AppDeployStatus;
import com.kintone.client.model.app.DeployApp;
import com.kintone.client.model.app.DeployStatus;
import com.kintone.client.model.app.field.Alignment;
import com.kintone.client.model.app.field.CalcFieldProperty;
import com.kintone.client.model.app.field.CheckBoxFieldProperty;
import com.kintone.client.model.app.field.DateFieldProperty;
import com.kintone.client.model.app.field.DateTimeFieldProperty;
import com.kintone.client.model.app.field.DisplayFormat;
import com.kintone.client.model.app.field.DropDownFieldProperty;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.app.field.FileFieldProperty;
import com.kintone.client.model.app.field.GroupFieldProperty;
import com.kintone.client.model.app.field.GroupSelectFieldProperty;
import com.kintone.client.model.app.field.LinkFieldProperty;
import com.kintone.client.model.app.field.LinkProtocol;
import com.kintone.client.model.app.field.LookupFieldProperty;
import com.kintone.client.model.app.field.LookupSetting;
import com.kintone.client.model.app.field.MultiLineTextFieldProperty;
import com.kintone.client.model.app.field.MultiSelectFieldProperty;
import com.kintone.client.model.app.field.NumberFieldProperty;
import com.kintone.client.model.app.field.Option;
import com.kintone.client.model.app.field.OrganizationSelectFieldProperty;
import com.kintone.client.model.app.field.RadioButtonFieldProperty;
import com.kintone.client.model.app.field.ReferenceTable;
import com.kintone.client.model.app.field.ReferenceTableCondition;
import com.kintone.client.model.app.field.ReferenceTableFieldProperty;
import com.kintone.client.model.app.field.RelatedApp;
import com.kintone.client.model.app.field.RichTextFieldProperty;
import com.kintone.client.model.app.field.SingleLineTextFieldProperty;
import com.kintone.client.model.app.field.SubtableFieldProperty;
import com.kintone.client.model.app.field.TimeFieldProperty;
import com.kintone.client.model.app.field.UnitPosition;
import com.kintone.client.model.app.field.UserSelectFieldProperty;
import com.kintone.client.model.record.*;
import com.kintone.client.model.record.Record;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.*;
import java.util.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AllFieldTypeAppTest extends ApiTestBase {

    private enum FieldCode {
        CALC,
        CHECK_BOX,
        CREATED_TIME,
        CREATOR,
        DATE,
        DATETIME,
        DROP_DOWN,
        FILE,
        GROUP,
        GROUP_SELECT,
        LINK,
        LOOKUP,
        MODIFIER,
        MULTI_LINE_TEXT,
        MULTI_SELECT,
        NUMBER,
        ORGANIZATION_SELECT,
        RADIO_BUTTON,
        REFERENCE_TABLE,
        RICH_TEXT,
        SINGLE_LINE_TEXT,
        SUBTABLE,
        TIME,
        UPDATED_TIME,
        USER_SELECT
    }

    private static final String LABEL_SUFFIX = "_LABEL";
    private static final LocalDateTime DATE_TIME_DEFAULT_VALUE =
            LocalDateTime.of(2023, 12, 22, 23, 45);
    private static final LocalDate DATE_FIELD_DEFAULT_VALUE = LocalDate.of(2023, 12, 22);
    private static final LocalTime TIME_FIELD_DEFAULT_VALUE = LocalTime.of(13, 45);

    private KintoneClient client;

    @BeforeEach
    public void setup() {
        client = setupDefaultClient();
    }

    @Test
    public void run() {
        // ルックアップなどメインアプリから参照するための最小限アプリ
        String relatedFieldCode = "RELATED_FIELD";
        long relatedAppId = addApp("RELATED_APP");
        client.app().addFormFields(relatedAppId, createMinimumFieldProperties(relatedFieldCode));
        deployApp(relatedAppId);
        waitDeployApp(relatedAppId);

        Record relatedRecord = new Record();
        relatedRecord.putField(relatedFieldCode, new NumberFieldValue(1L));
        client.record().addRecord(relatedAppId, relatedRecord);

        // 全フィールドを持つアプリ
        long appId = addApp("ALL_FIELD_APP");
        client.app().addFormFields(appId, createAllFieldProperties(relatedAppId, relatedFieldCode));
        deployApp(appId);
        waitDeployApp(appId);

        String fileKey = uploadFile();
        Record newRecord = createAllFieldSettingRecord(fileKey);
        long recordId = client.record().addRecord(appId, newRecord);

        // 表示形式が指定してある計算フィールドの値の扱いの確認
        Record record = client.record().getRecord(appId, recordId);
        String value = record.getCalcFieldValue("CALC_DATE");
        assertThat(value).isEqualTo("2022-01-02");

        // 日時フィールドの初期値がローカルタイムになっているかの確認
        Map<String, FieldProperty> fields = client.app().getFormFields(appId);
        String dateTimeFieldCode = FieldCode.DATETIME.name() + "WithDefault";
        LocalDateTime dateTime =
                ((DateTimeFieldProperty) fields.get(dateTimeFieldCode)).getDefaultValue();
        assertThat(dateTime).isEqualTo(DATE_TIME_DEFAULT_VALUE);
        // タイムゾーンはJSTを想定
        LocalDateTime localDateTime =
                record
                        .getDateTimeFieldValue(dateTimeFieldCode)
                        .withZoneSameInstant(ZoneId.of("Asia/Tokyo"))
                        .toLocalDateTime();
        assertThat(localDateTime).isEqualTo(DATE_TIME_DEFAULT_VALUE);

        String dateFieldCode = FieldCode.DATE.name() + "WithDefault";
        LocalDate date = ((DateFieldProperty) fields.get(dateFieldCode)).getDefaultValue();
        assertThat(date).isEqualTo(DATE_FIELD_DEFAULT_VALUE);
        assertThat(record.getDateFieldValue(dateFieldCode)).isEqualTo(DATE_FIELD_DEFAULT_VALUE);

        String timeFieldCode = FieldCode.TIME.name() + "WithDefault";
        LocalTime time = ((TimeFieldProperty) fields.get(timeFieldCode)).getDefaultValue();
        assertThat(time).isEqualTo(TIME_FIELD_DEFAULT_VALUE);
        assertThat(record.getTimeFieldValue(timeFieldCode)).isEqualTo(TIME_FIELD_DEFAULT_VALUE);
    }

    private long addApp(String name) {
        AddAppRequest request = new AddAppRequest();
        request.setName(name);
        AddAppResponseBody response = client.app().addApp(request);
        return response.getApp();
    }

    private void deployApp(long appId) {
        DeployApp deployApp = new DeployApp();
        deployApp.setApp(appId);
        DeployAppRequest request = new DeployAppRequest();
        request.setApps(Collections.singletonList(deployApp));
        client.app().deployApp(request);
    }

    private void waitDeployApp(long appId) {
        GetDeployStatusRequest request = new GetDeployStatusRequest();
        request.setApps(Collections.singletonList(appId));
        boolean isApplied = false;
        while (!isApplied) {
            GetDeployStatusResponseBody response = client.app().getDeployStatus(request);
            for (AppDeployStatus app : response.getApps()) {
                if (app.getStatus().equals(DeployStatus.SUCCESS)) {
                    isApplied = true;
                    break;
                }
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String uploadFile() {
        File file = null;
        String fileKey = "";
        try {
            file = File.createTempFile("kintone-AllFieldTypeAppTest-", ".txt");
            Files.write(file.toPath(), FieldCode.FILE.name().getBytes());
            fileKey = client.file().uploadFile(file.toPath(), "text/plain");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.delete();
        }
        return fileKey;
    }

    private Map<String, FieldProperty> createMinimumFieldProperties(String relatedFieldCode) {
        NumberFieldProperty field = new NumberFieldProperty();
        field.setCode(relatedFieldCode);
        field.setLabel(relatedFieldCode + LABEL_SUFFIX);
        field.setUnique(true);

        Map<String, FieldProperty> properties = new HashMap<>();
        properties.put(field.getCode(), field);
        return properties;
    }

    private Map<String, FieldProperty> createAllFieldProperties(
            long relatedAppId, String relatedFieldCode) {
        // フィールド共通で使用
        // 選択肢
        Option option1 = new Option();
        option1.setLabel("option1");
        option1.setIndex(0L);

        Option option2 = new Option();
        option2.setLabel("option2");
        option2.setIndex(1L);

        Map<String, Option> options = new HashMap<>();
        options.put(option1.getLabel(), option1);
        options.put(option2.getLabel(), option2);

        // 関連アプリ
        RelatedApp relatedApp = new RelatedApp();
        relatedApp.setApp(relatedAppId);

        // 各フィールド設定
        CalcFieldProperty calcField = new CalcFieldProperty();
        calcField.setCode(FieldCode.CALC.name());
        calcField.setLabel(FieldCode.CALC.name() + LABEL_SUFFIX);
        calcField.setExpression(FieldCode.NUMBER.name() + "*2");
        calcField.setFormat(DisplayFormat.NUMBER_DIGIT);
        calcField.setUnit("YEN");
        calcField.setUnitPosition(UnitPosition.AFTER);
        calcField.setDisplayScale(0L);

        CalcFieldProperty calcDateTimeField = new CalcFieldProperty();
        calcDateTimeField.setCode(FieldCode.CALC.name() + "_DATETIME");
        calcDateTimeField.setLabel(calcDateTimeField.getCode() + LABEL_SUFFIX);
        calcDateTimeField.setExpression(FieldCode.NUMBER.name() + "+1641081598");
        calcDateTimeField.setFormat(DisplayFormat.DATETIME);

        CalcFieldProperty calcDateField = new CalcFieldProperty();
        calcDateField.setCode(FieldCode.CALC.name() + "_DATE");
        calcDateField.setLabel(calcDateField.getCode() + LABEL_SUFFIX);
        calcDateField.setExpression(FieldCode.NUMBER.name() + "+1641081598"); // 2022/1/2 (UTC) になる
        calcDateField.setFormat(DisplayFormat.DATE);

        CalcFieldProperty calcTimeField = new CalcFieldProperty();
        calcTimeField.setCode(FieldCode.CALC.name() + "_TIME");
        calcTimeField.setLabel(calcTimeField.getCode() + LABEL_SUFFIX);
        calcTimeField.setExpression(FieldCode.NUMBER.name() + "+0");
        calcTimeField.setFormat(DisplayFormat.TIME);

        CalcFieldProperty calcDaysField = new CalcFieldProperty();
        calcDaysField.setCode(FieldCode.CALC.name() + "_DAYS");
        calcDaysField.setLabel(calcDaysField.getCode() + LABEL_SUFFIX);
        calcDaysField.setExpression("304245"); // 3日と12時間30分45秒
        calcDaysField.setFormat(DisplayFormat.DAY_HOUR_MINUTE);

        CheckBoxFieldProperty checkBoxField = new CheckBoxFieldProperty();
        checkBoxField.setCode(FieldCode.CHECK_BOX.name());
        checkBoxField.setLabel(FieldCode.CHECK_BOX.name() + LABEL_SUFFIX);
        checkBoxField.setNoLabel(false);
        checkBoxField.setRequired(false);
        checkBoxField.setOptions(options);
        checkBoxField.setAlign(Alignment.VERTICAL);
        checkBoxField.setDefaultValue(Lists.newArrayList(option2.getLabel()));

        DateFieldProperty dateField = new DateFieldProperty();
        dateField.setCode(FieldCode.DATE.name());
        dateField.setLabel(FieldCode.DATE.name() + LABEL_SUFFIX);

        DateFieldProperty dateFieldWithDefault = new DateFieldProperty();
        dateFieldWithDefault.setCode(FieldCode.DATE.name() + "WithDefault");
        dateFieldWithDefault.setLabel(FieldCode.DATE.name() + "WithDefault" + LABEL_SUFFIX);
        dateFieldWithDefault.setDefaultValue(DATE_FIELD_DEFAULT_VALUE);
        dateFieldWithDefault.setDefaultNowValue(false);

        DateTimeFieldProperty dateTimeField = new DateTimeFieldProperty();
        dateTimeField.setCode(FieldCode.DATETIME.name());
        dateTimeField.setLabel(FieldCode.DATETIME.name() + LABEL_SUFFIX);

        DateTimeFieldProperty dateTimeFieldWithDefault = new DateTimeFieldProperty();
        dateTimeFieldWithDefault.setCode(FieldCode.DATETIME.name() + "WithDefault");
        dateTimeFieldWithDefault.setLabel(FieldCode.DATETIME.name() + "WithDefault" + LABEL_SUFFIX);
        dateTimeFieldWithDefault.setDefaultValue(DATE_TIME_DEFAULT_VALUE);
        dateTimeFieldWithDefault.setDefaultNowValue(false);

        DropDownFieldProperty dropDownField = new DropDownFieldProperty();
        dropDownField.setCode(FieldCode.DROP_DOWN.name());
        dropDownField.setLabel(FieldCode.DROP_DOWN.name() + LABEL_SUFFIX);
        dropDownField.setOptions(options);

        FileFieldProperty fileField = new FileFieldProperty();
        fileField.setCode(FieldCode.FILE.name());
        fileField.setLabel(FieldCode.FILE.name() + LABEL_SUFFIX);
        fileField.setThumbnailSize(250L);

        GroupFieldProperty groupField = new GroupFieldProperty();
        groupField.setCode(FieldCode.GROUP.name());
        groupField.setLabel(FieldCode.GROUP.name() + LABEL_SUFFIX);
        groupField.setOpenGroup(true);

        GroupSelectFieldProperty groupSelectField = new GroupSelectFieldProperty();
        groupSelectField.setCode(FieldCode.GROUP_SELECT.name());
        groupSelectField.setLabel(FieldCode.GROUP_SELECT.name() + LABEL_SUFFIX);
        groupSelectField.setDefaultValue(Lists.newArrayList(new Entity(EntityType.GROUP, "everyone")));

        LinkFieldProperty linkField = new LinkFieldProperty();
        linkField.setCode(FieldCode.LINK.name());
        linkField.setLabel(FieldCode.LINK.name() + LABEL_SUFFIX);
        linkField.setProtocol(LinkProtocol.WEB);
        linkField.setMinLength(1L);
        linkField.setMaxLength(256L);
        linkField.setDefaultValue("http://localhost/k");

        LookupSetting lookupSetting = new LookupSetting();
        lookupSetting.setRelatedApp(relatedApp);
        lookupSetting.setRelatedKeyField(relatedFieldCode);

        LookupFieldProperty lookupField = new LookupFieldProperty(FieldType.NUMBER);
        lookupField.setCode(FieldCode.LOOKUP.name());
        lookupField.setLabel(FieldCode.LOOKUP.name() + LABEL_SUFFIX);
        lookupField.setLookup(lookupSetting);

        MultiLineTextFieldProperty multiLineTextField = new MultiLineTextFieldProperty();
        multiLineTextField.setCode(FieldCode.MULTI_LINE_TEXT.name());
        multiLineTextField.setLabel(FieldCode.MULTI_LINE_TEXT.name() + LABEL_SUFFIX);
        multiLineTextField.setDefaultValue("Test\nABC\nDEF");

        MultiSelectFieldProperty multiSelectField = new MultiSelectFieldProperty();
        multiSelectField.setCode(FieldCode.MULTI_SELECT.name());
        multiSelectField.setLabel(FieldCode.MULTI_SELECT.name() + LABEL_SUFFIX);
        multiSelectField.setOptions(options);
        multiSelectField.setDefaultValue(Lists.newArrayList(option1.getLabel()));

        NumberFieldProperty numberField = new NumberFieldProperty();
        numberField.setCode(FieldCode.NUMBER.name());
        numberField.setLabel(FieldCode.NUMBER.name() + LABEL_SUFFIX);
        numberField.setDigit(true);
        numberField.setUnit("YEN");
        numberField.setUnitPosition(UnitPosition.AFTER);
        numberField.setDisplayScale(0L);
        numberField.setDefaultValue(BigDecimal.valueOf(0L));

        OrganizationSelectFieldProperty organizationSelectField = new OrganizationSelectFieldProperty();
        organizationSelectField.setCode(FieldCode.ORGANIZATION_SELECT.name());
        organizationSelectField.setLabel(FieldCode.ORGANIZATION_SELECT.name() + LABEL_SUFFIX);

        RadioButtonFieldProperty radioButtonField = new RadioButtonFieldProperty();
        radioButtonField.setCode(FieldCode.RADIO_BUTTON.name());
        radioButtonField.setLabel(FieldCode.RADIO_BUTTON.name() + LABEL_SUFFIX);
        radioButtonField.setOptions(options);
        radioButtonField.setAlign(Alignment.VERTICAL);
        radioButtonField.setDefaultValue(option2.getLabel());

        ReferenceTableCondition referenceTableCondition = new ReferenceTableCondition();
        referenceTableCondition.setField(FieldCode.LOOKUP.name());
        referenceTableCondition.setRelatedField(relatedFieldCode);

        ReferenceTable referenceTable = new ReferenceTable();
        referenceTable.setRelatedApp(relatedApp);
        referenceTable.setCondition(referenceTableCondition);
        referenceTable.setDisplayFields(Collections.singletonList(relatedFieldCode));

        ReferenceTableFieldProperty referenceTableField = new ReferenceTableFieldProperty();
        referenceTableField.setCode(FieldCode.REFERENCE_TABLE.name());
        referenceTableField.setLabel(FieldCode.REFERENCE_TABLE.name() + LABEL_SUFFIX);
        referenceTableField.setReferenceTable(referenceTable);

        RichTextFieldProperty richTextField = new RichTextFieldProperty();
        richTextField.setCode(FieldCode.RICH_TEXT.name());
        richTextField.setLabel(FieldCode.RICH_TEXT.name() + LABEL_SUFFIX);
        richTextField.setDefaultValue("<div>HTML</div>");

        SingleLineTextFieldProperty singleLineTextField = new SingleLineTextFieldProperty();
        singleLineTextField.setCode(FieldCode.SINGLE_LINE_TEXT.name());
        singleLineTextField.setLabel(FieldCode.SINGLE_LINE_TEXT.name() + LABEL_SUFFIX);

        SingleLineTextFieldProperty subtableChildField = new SingleLineTextFieldProperty();
        subtableChildField.setCode("subtableChildField");
        subtableChildField.setLabel(subtableChildField.getCode() + LABEL_SUFFIX);

        Map<String, FieldProperty> subtableFields = new HashMap<>();
        subtableFields.put(subtableChildField.getCode(), subtableChildField);

        SubtableFieldProperty subtableField = new SubtableFieldProperty();
        subtableField.setCode(FieldCode.SUBTABLE.name());
        subtableField.setLabel(FieldCode.SUBTABLE.name() + LABEL_SUFFIX);
        subtableField.setFields(subtableFields);

        TimeFieldProperty timeField = new TimeFieldProperty();
        timeField.setCode(FieldCode.TIME.name());
        timeField.setLabel(FieldCode.TIME.name() + LABEL_SUFFIX);

        TimeFieldProperty timeFieldWithDefault = new TimeFieldProperty();
        timeFieldWithDefault.setCode(FieldCode.TIME.name() + "WithDefault");
        timeFieldWithDefault.setLabel(FieldCode.TIME.name() + "WithDefault" + LABEL_SUFFIX);
        timeFieldWithDefault.setDefaultValue(TIME_FIELD_DEFAULT_VALUE);
        timeFieldWithDefault.setDefaultNowValue(false);

        UserSelectFieldProperty userSelectField = new UserSelectFieldProperty();
        userSelectField.setCode(FieldCode.USER_SELECT.name());
        userSelectField.setLabel(FieldCode.USER_SELECT.name() + LABEL_SUFFIX);
        userSelectField.setDefaultValue(
                Lists.newArrayList(new Entity(EntityType.USER, getDefaultUser())));

        UserSelectFieldProperty userSelectField2 = new UserSelectFieldProperty();
        userSelectField2.setCode(FieldCode.USER_SELECT.name() + "2");
        userSelectField2.setLabel(userSelectField2.getCode() + LABEL_SUFFIX);
        userSelectField2.setDefaultValue(
                Lists.newArrayList(new Entity(EntityType.USER, getDefaultUser())));
        userSelectField2.setEntities(Lists.newArrayList(new Entity(EntityType.USER, getDefaultUser())));

        List<FieldProperty> fieldList =
                Arrays.asList(
                        calcField,
                        calcDateTimeField,
                        calcDateField,
                        calcTimeField,
                        calcDaysField,
                        checkBoxField,
                        dateField,
                        dateFieldWithDefault,
                        dateTimeField,
                        dateTimeFieldWithDefault,
                        dropDownField,
                        fileField,
                        groupField,
                        groupSelectField,
                        linkField,
                        lookupField,
                        multiLineTextField,
                        multiSelectField,
                        numberField,
                        organizationSelectField,
                        radioButtonField,
                        referenceTableField,
                        richTextField,
                        singleLineTextField,
                        subtableField,
                        timeField,
                        timeFieldWithDefault,
                        userSelectField,
                        userSelectField2);

        Map<String, FieldProperty> properties = new HashMap<>();
        for (FieldProperty field : fieldList) {
            properties.put(field.getCode(), field);
        }
        return properties;
    }

    private Record createAllFieldSettingRecord(String fileKey) {
        ZonedDateTime datetime = ZonedDateTime.of(2020, 1, 2, 3, 4, 5, 6, ZoneOffset.UTC);
        User user = new User("", "Administrator");

        FileBody fileBody = new FileBody();
        fileBody.setFileKey(fileKey);

        TableRow tableRow = new TableRow();
        tableRow.putField("subtableChildField", new SingleLineTextFieldValue("subtableChild"));

        Record record = new Record();
        record.putField(FieldCode.CALC.name(), new CalcFieldValue("10000"));
        record.putField(FieldCode.CALC.name() + "_DATE", new CalcFieldValue("2000-01-01"));
        record.putField(FieldCode.CHECK_BOX.name(), new CheckBoxFieldValue("option1", "option2"));
        record.putField(
                FieldCode.CREATED_TIME.name(), new CreatedTimeFieldValue(datetime.plusDays(1L)));
        record.putField(FieldCode.CREATOR.name(), new CreatorFieldValue(user));
        record.putField(FieldCode.DATE.name(), new DateFieldValue(datetime.plusDays(1L).toLocalDate()));
        record.putField(FieldCode.DATETIME.name(), new DateTimeFieldValue(datetime.plusDays(1L)));
        record.putField(FieldCode.DROP_DOWN.name(), new DropDownFieldValue("option1"));
        record.putField(FieldCode.FILE.name(), new FileFieldValue(fileBody));
        record.putField(
                FieldCode.GROUP_SELECT.name(), new GroupSelectFieldValue(new Group("", "Administrators")));
        record.putField(FieldCode.LINK.name(), new LinkFieldValue("https://www.cybozu.com/"));
        record.putField(FieldCode.LOOKUP.name(), new NumberFieldValue(1L));
        record.putField(FieldCode.MODIFIER.name(), new ModifierFieldValue(user));
        record.putField(FieldCode.MULTI_LINE_TEXT.name(), new MultiLineTextFieldValue("multiLineText"));
        record.putField(FieldCode.MULTI_SELECT.name(), new MultiSelectFieldValue("option1", "option2"));
        record.putField(FieldCode.NUMBER.name(), new NumberFieldValue(2L));
        record.putField(
                FieldCode.ORGANIZATION_SELECT.name(),
                new OrganizationSelectFieldValue(new Organization("", "dev")));
        record.putField(FieldCode.RADIO_BUTTON.name(), new RadioButtonFieldValue("option2"));
        record.putField(FieldCode.RICH_TEXT.name(), new RichTextFieldValue("richText"));
        record.putField(
                FieldCode.SINGLE_LINE_TEXT.name(), new SingleLineTextFieldValue("singleLineText"));
        record.putField(FieldCode.SUBTABLE.name(), new SubtableFieldValue(tableRow, tableRow));
        record.putField(FieldCode.TIME.name(), new TimeFieldValue(datetime.plusDays(1L).toLocalTime()));
        record.putField(
                FieldCode.UPDATED_TIME.name(), new UpdatedTimeFieldValue(datetime.plusDays(1L)));
        record.putField(FieldCode.USER_SELECT.name(), new UserSelectFieldValue(user));
        return record;
    }
}
