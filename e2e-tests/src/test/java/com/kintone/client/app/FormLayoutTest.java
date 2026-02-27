package com.kintone.client.app;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.api.app.*;
import com.kintone.client.helper.App;
import com.kintone.client.helper.Fields;
import com.kintone.client.helper.FormLayoutBuilder;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.app.layout.*;
import com.kintone.client.model.record.FieldType;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/** AppClientのlayout.jsonのテスト */
public class FormLayoutTest extends ApiTestBase {
    @Test
    public void getFormLayout_getFormLayoutPreview() {
        KintoneClient client = setupDefaultClient();
        App app = App.create(client, "getFormLayout_getFormLayoutPreview");
        FieldProperty text = Fields.text();
        app.addFields(text);

        FormLayoutBuilder builder = new FormLayoutBuilder();
        builder.row().field(text, 200).hr(100);
        builder.row().label("sample label", 200).spacer("spacer", 100, 100);
        app.updateLayout(builder).deploy();
        long revision = app.getAppRevision(true);

        GetFormLayoutRequest req1 = new GetFormLayoutRequest();
        req1.setApp(app.id());
        GetFormLayoutResponseBody resp1 = client.app().getFormLayout(req1);
        assertThat(resp1.getRevision()).isEqualTo(revision);
        assertThat(resp1.getLayout())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(builder.build());

        builder = new FormLayoutBuilder().row().field(text, 200);
        app.updateLayout(builder);

        GetFormLayoutPreviewRequest req2 = new GetFormLayoutPreviewRequest();
        req2.setApp(app.id());
        GetFormLayoutPreviewResponseBody resp2 = client.app().getFormLayoutPreview(req2);
        assertThat(resp2.getRevision()).isEqualTo(revision + 1);
        assertThat(resp2.getLayout())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(builder.build());
    }

    @Test
    public void updateFormLayout() {
        KintoneClient client = setupDefaultClient();
        App app = App.create(client, "updateFormLayout");
        FieldProperty text = Fields.text();
        app.addFields(text);
        long revision = app.getAppRevision(true);

        List<FieldLayout> row1 = new ArrayList<>();
        row1.add(
                new FieldLayout()
                        .setType(text.getType())
                        .setCode(text.getCode())
                        .setSize(new FieldSize().setWidth(200)));
        row1.add(
                new FieldLayout()
                        .setType(FieldType.SPACER)
                        .setElementId("spacer")
                        .setSize(new FieldSize().setWidth(100).setHeight(80)));
        List<FieldLayout> row2 = new ArrayList<>();
        row2.add(
                new FieldLayout()
                        .setType(FieldType.HR)
                        .setElementId("")
                        .setSize(new FieldSize().setWidth(300)));

        List<Layout> layout = new ArrayList<>();
        layout.add(new RowLayout().setFields(row1));
        layout.add(new RowLayout().setFields(row2));

        UpdateFormLayoutRequest req = new UpdateFormLayoutRequest();
        req.setApp(app.id());
        req.setLayout(layout);
        req.setRevision(revision);
        UpdateFormLayoutResponseBody resp = client.app().updateFormLayout(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);

        List<Layout> updatedLayout = app.getLayout(true);
        assertThat(updatedLayout).usingRecursiveFieldByFieldElementComparator().isEqualTo(layout);
    }
}
