package com.kintone.client.app;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.api.app.*;
import com.kintone.client.helper.App;
import com.kintone.client.helper.Fields;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.app.field.NumberFieldProperty;
import com.kintone.client.model.app.field.SingleLineTextFieldProperty;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** AppClientのfields.jsonのテスト */
public class FormFieldsTest extends ApiTestBase {
    @Test
    public void addFormFields() {
        KintoneClient client = setupDefaultClient();
        App app = App.create(client, "addFormFields");
        long revision = app.getAppRevision(true);

        Map<String, FieldProperty> fields = new HashMap<>();
        fields.put("text", Fields.text("text"));
        fields.put("number", Fields.number("number"));

        AddFormFieldsRequest req = new AddFormFieldsRequest();
        req.setApp(app.id());
        req.setProperties(fields);
        req.setRevision(revision);
        AddFormFieldsResponseBody resp = client.app().addFormFields(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);

        Map<String, FieldProperty> updatedFields = app.getFields(true);
        assertThat(updatedFields).containsKeys("text", "number");
    }

    @Test
    public void deleteFormFields() {
        KintoneClient client = setupDefaultClient();
        FieldProperty text = Fields.text();
        FieldProperty number = Fields.number();
        FieldProperty userSelect = Fields.userSelect();
        App app = App.create(client, "deleteFormFields");
        app.addFields(text, number, userSelect);
        long revision = app.getAppRevision(true);

        DeleteFormFieldsRequest req = new DeleteFormFieldsRequest();
        req.setApp(app.id());
        req.setFields(Arrays.asList(text.getCode(), userSelect.getCode()));
        req.setRevision(revision);
        DeleteFormFieldsResponseBody resp = client.app().deleteFormFields(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);

        Map<String, FieldProperty> updatedFields = app.getFields(true);
        assertThat(updatedFields).containsKeys(number.getCode());
        assertThat(updatedFields).doesNotContainKeys(text.getCode(), userSelect.getCode());
    }

    @Test
    public void getFormFields_getFormFieldsPreview() {
        KintoneClient client = setupDefaultClient();
        FieldProperty text = Fields.text().setExpression("\"ABC\"");
        FieldProperty number = Fields.number().setDefaultValue(BigDecimal.valueOf(100));
        App app = App.create(client, "getFormFields_getFormFieldsPreview");
        app.addFields(text, number).deploy();
        long revision = app.getAppRevision(false);

        GetFormFieldsRequest req1 = new GetFormFieldsRequest();
        req1.setApp(app.id());
        GetFormFieldsResponseBody resp1 = client.app().getFormFields(req1);
        assertThat(resp1.getRevision()).isEqualTo(revision);
        Map<String, FieldProperty> fields = resp1.getProperties();
        assertThat(fields).hasSize(10); // レコード番号を除く組み込みフィールド + 2フィールド
        SingleLineTextFieldProperty p1 = (SingleLineTextFieldProperty) fields.get(text.getCode());
        assertThat(p1.getExpression()).isEqualTo("\"ABC\"");
        NumberFieldProperty p2 = (NumberFieldProperty) fields.get(number.getCode());
        assertThat(p2.getDefaultValue()).isEqualTo(BigDecimal.valueOf(100));

        app.deleteFields(text.getCode());
        GetFormFieldsPreviewRequest req2 = new GetFormFieldsPreviewRequest();
        req2.setApp(app.id());
        GetFormFieldsPreviewResponseBody resp2 = client.app().getFormFieldsPreview(req2);
        assertThat(resp2.getRevision()).isEqualTo(revision + 1);
        fields = resp2.getProperties();
        assertThat(fields).hasSize(9);
        NumberFieldProperty p3 = (NumberFieldProperty) fields.get(number.getCode());
        assertThat(p3.getDefaultValue()).isEqualTo(BigDecimal.valueOf(100));
    }

    @Test
    public void updateFormFields() {
        KintoneClient client = setupDefaultClient();
        FieldProperty text = Fields.text();
        FieldProperty number = Fields.number();
        FieldProperty userSelect = Fields.userSelect();
        App app = App.create(client, "updateFormFields");
        app.addFields(text, number, userSelect);
        long revision = app.getAppRevision(true);

        Map<String, FieldProperty> fields = new HashMap<>();
        fields.put(text.getCode(), Fields.text("A").setUnique(true));
        fields.put(number.getCode(), Fields.number("B").setRequired(true));

        UpdateFormFieldsRequest req = new UpdateFormFieldsRequest();
        req.setApp(app.id());
        req.setProperties(fields);
        req.setRevision(revision);
        UpdateFormFieldsResponseBody resp = client.app().updateFormFields(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);

        Map<String, FieldProperty> updatedFields = app.getFields(true);
        assertThat(updatedFields).containsKeys("A", "B", userSelect.getCode());
        assertThat(updatedFields).doesNotContainKeys(text.getCode(), number.getCode());
        SingleLineTextFieldProperty p1 = (SingleLineTextFieldProperty) updatedFields.get("A");
        assertThat(p1.getUnique()).isTrue();
        NumberFieldProperty p2 = (NumberFieldProperty) updatedFields.get("B");
        assertThat(p2.getRequired()).isTrue();
    }
}
