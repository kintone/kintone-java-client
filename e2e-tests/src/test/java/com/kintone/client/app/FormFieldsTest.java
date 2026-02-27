package com.kintone.client.app;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.TestSettings;
import com.kintone.client.api.app.*;
import com.kintone.client.helper.App;
import com.kintone.client.helper.Fields;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.app.field.NumberFieldProperty;
import com.kintone.client.model.app.field.SingleLineTextFieldProperty;
import java.math.BigDecimal;
import java.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** AppClientのfields.jsonのテスト */
public class FormFieldsTest extends ApiTestBase {

    private KintoneClient client;
    private App app;
    private Set<String> addedFieldCodes = new HashSet<>();

    @BeforeEach
    public void setupApp() {
        client = setupDefaultClient();
        Long testAppId = TestSettings.get().getTestAppId();
        if (testAppId != null) {
            app = App.fromExisting(client, testAppId);
        } else {
            throw new IllegalStateException(
                    "KINTONE_TEST_APP_ID is not set. Please create a test app and set the environment variable.");
        }
        addedFieldCodes.clear();
    }

    @AfterEach
    public void cleanupFields() {
        if (app != null && !addedFieldCodes.isEmpty()) {
            try {
                client.app().deleteFormFields(app.id(), new ArrayList<>(addedFieldCodes));
                client.app().deployApp(app.id());
                app.waitDeploy();
            } catch (Exception e) {
                // ignore cleanup errors
            }
        }
    }

    @Test
    public void addFormFields() {
        long revision = app.getAppRevision(true);

        Map<String, FieldProperty> fields = new HashMap<>();
        String textCode = "test_text_" + System.currentTimeMillis();
        String numberCode = "test_number_" + System.currentTimeMillis();
        fields.put(textCode, Fields.text(textCode));
        fields.put(numberCode, Fields.number(numberCode));

        AddFormFieldsRequest req = new AddFormFieldsRequest();
        req.setApp(app.id());
        req.setProperties(fields);
        req.setRevision(revision);
        AddFormFieldsResponseBody resp = client.app().addFormFields(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);

        addedFieldCodes.add(textCode);
        addedFieldCodes.add(numberCode);

        Map<String, FieldProperty> updatedFields = app.getFields(true);
        assertThat(updatedFields).containsKeys(textCode, numberCode);
    }

    @Test
    public void deleteFormFields() {
        String textCode = "test_text_" + System.currentTimeMillis();
        String numberCode = "test_number_" + System.currentTimeMillis();
        String userSelectCode = "test_user_" + System.currentTimeMillis();

        FieldProperty text = Fields.text(textCode);
        FieldProperty number = Fields.number(numberCode);
        FieldProperty userSelect = Fields.userSelect(userSelectCode);
        app.addFields(text, number, userSelect);
        long revision = app.getAppRevision(true);

        DeleteFormFieldsRequest req = new DeleteFormFieldsRequest();
        req.setApp(app.id());
        req.setFields(Arrays.asList(textCode, userSelectCode));
        req.setRevision(revision);
        DeleteFormFieldsResponseBody resp = client.app().deleteFormFields(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);

        // numberCodeは削除されていないのでクリーンアップ対象
        addedFieldCodes.add(numberCode);

        Map<String, FieldProperty> updatedFields = app.getFields(true);
        assertThat(updatedFields).containsKeys(numberCode);
        assertThat(updatedFields).doesNotContainKeys(textCode, userSelectCode);
    }

    @Test
    public void getFormFields_getFormFieldsPreview() {
        String textCode = "test_text_" + System.currentTimeMillis();
        String numberCode = "test_number_" + System.currentTimeMillis();

        FieldProperty text = Fields.text(textCode).setExpression("\"ABC\"");
        FieldProperty number = Fields.number(numberCode).setDefaultValue(BigDecimal.valueOf(100));
        app.addFields(text, number).deploy();
        addedFieldCodes.add(textCode);
        addedFieldCodes.add(numberCode);
        long revision = app.getAppRevision(false);

        GetFormFieldsRequest req1 = new GetFormFieldsRequest();
        req1.setApp(app.id());
        GetFormFieldsResponseBody resp1 = client.app().getFormFields(req1);
        assertThat(resp1.getRevision()).isEqualTo(revision);
        Map<String, FieldProperty> fields = resp1.getProperties();
        SingleLineTextFieldProperty p1 = (SingleLineTextFieldProperty) fields.get(textCode);
        assertThat(p1.getExpression()).isEqualTo("\"ABC\"");
        NumberFieldProperty p2 = (NumberFieldProperty) fields.get(numberCode);
        assertThat(p2.getDefaultValue()).isEqualTo(BigDecimal.valueOf(100));

        app.deleteFields(textCode);
        addedFieldCodes.remove(textCode);
        GetFormFieldsPreviewRequest req2 = new GetFormFieldsPreviewRequest();
        req2.setApp(app.id());
        GetFormFieldsPreviewResponseBody resp2 = client.app().getFormFieldsPreview(req2);
        assertThat(resp2.getRevision()).isEqualTo(revision + 1);
        fields = resp2.getProperties();
        assertThat(fields).doesNotContainKey(textCode);
        NumberFieldProperty p3 = (NumberFieldProperty) fields.get(numberCode);
        assertThat(p3.getDefaultValue()).isEqualTo(BigDecimal.valueOf(100));
    }

    @Test
    public void updateFormFields() {
        String textCode = "test_text_" + System.currentTimeMillis();
        String numberCode = "test_number_" + System.currentTimeMillis();
        String userSelectCode = "test_user_" + System.currentTimeMillis();
        String newTextCode = "test_A_" + System.currentTimeMillis();
        String newNumberCode = "test_B_" + System.currentTimeMillis();

        FieldProperty text = Fields.text(textCode);
        FieldProperty number = Fields.number(numberCode);
        FieldProperty userSelect = Fields.userSelect(userSelectCode);
        app.addFields(text, number, userSelect);
        long revision = app.getAppRevision(true);

        Map<String, FieldProperty> fields = new HashMap<>();
        fields.put(textCode, Fields.text(newTextCode).setUnique(true));
        fields.put(numberCode, Fields.number(newNumberCode).setRequired(true));

        UpdateFormFieldsRequest req = new UpdateFormFieldsRequest();
        req.setApp(app.id());
        req.setProperties(fields);
        req.setRevision(revision);
        UpdateFormFieldsResponseBody resp = client.app().updateFormFields(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);

        // 更新後のフィールドコードをクリーンアップ対象に
        addedFieldCodes.add(newTextCode);
        addedFieldCodes.add(newNumberCode);
        addedFieldCodes.add(userSelectCode);

        Map<String, FieldProperty> updatedFields = app.getFields(true);
        assertThat(updatedFields).containsKeys(newTextCode, newNumberCode, userSelectCode);
        assertThat(updatedFields).doesNotContainKeys(textCode, numberCode);
        SingleLineTextFieldProperty p1 = (SingleLineTextFieldProperty) updatedFields.get(newTextCode);
        assertThat(p1.getUnique()).isTrue();
        NumberFieldProperty p2 = (NumberFieldProperty) updatedFields.get(newNumberCode);
        assertThat(p2.getRequired()).isTrue();
    }
}
