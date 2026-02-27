package com.kintone.client.app;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.TestSettings;
import com.kintone.client.api.app.*;
import com.kintone.client.helper.App;
import com.kintone.client.helper.FormLayoutBuilder;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.app.layout.*;
import com.kintone.client.model.record.FieldType;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** AppClientのlayout.jsonのテスト */
public class FormLayoutTest extends ApiTestBase {

    private static final String TEXT_FIELD_CODE = "文字列__1行_";

    private KintoneClient client;
    private App app;
    private App appForGetFormLayoutPreview;
    private List<Layout> originalLayout;

    @BeforeEach
    public void setupApp() {
        client = setupDefaultClient();
        Long testAppId = TestSettings.get().getTestAppId();
        Long testAppIdForGetFormLayoutPreview =
                TestSettings.get().getTestAppIdForGetFormLayoutPreview();
        if (testAppId != null) {
            app = App.fromExisting(client, testAppId);
        } else {
            throw new IllegalStateException(
                    "KINTONE_TEST_APP_ID is not set. Please create a test app and set the environment variable.");
        }
        if (testAppIdForGetFormLayoutPreview != null) {
            appForGetFormLayoutPreview = App.fromExisting(client, testAppIdForGetFormLayoutPreview);
        } else {
            throw new IllegalStateException(
                    "KINTONE_TEST_APP_ID_FOR_GET_FORM_LAYOUT_PREVIEW is not set. Please create a test app and set the environment variable.");
        }
        // 元のレイアウト設定を保存
        originalLayout = app.getLayout(false);
    }

    @AfterEach
    public void cleanupLayout() {
        if (app != null) {
            try {
                // 未デプロイの変更がある場合はリバートしてからデプロイ
                client.app().revertApp(app.id());
            } catch (Exception e) {
                // ignore cleanup errors (revert fails if no changes)
            }
        }
    }

    @Test
    public void getFormLayout_getFormLayoutPreview() {
        FieldProperty text = app.field(TEXT_FIELD_CODE);

        FormLayoutBuilder builder = new FormLayoutBuilder();
        builder.row().field(text, 200).hr(100);
        builder.row().label("sample label", 200).spacer("spacer", 100, 100);
        appForGetFormLayoutPreview.updateLayout(builder).deploy();
        long revision = appForGetFormLayoutPreview.getAppRevision(true);

        GetFormLayoutRequest req1 = new GetFormLayoutRequest();
        req1.setApp(appForGetFormLayoutPreview.id());
        GetFormLayoutResponseBody resp1 = client.app().getFormLayout(req1);
        assertThat(resp1.getRevision()).isEqualTo(revision);
        assertThat(resp1.getLayout())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(builder.build());

        builder = new FormLayoutBuilder().row().field(text, 200);
        appForGetFormLayoutPreview.updateLayout(builder);

        GetFormLayoutPreviewRequest req2 = new GetFormLayoutPreviewRequest();
        req2.setApp(appForGetFormLayoutPreview.id());
        GetFormLayoutPreviewResponseBody resp2 = client.app().getFormLayoutPreview(req2);
        assertThat(resp2.getRevision()).isEqualTo(revision + 1);
        assertThat(resp2.getLayout())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(builder.build());
    }

    @Test
    public void updateFormLayout() {
        FieldProperty text = appForGetFormLayoutPreview.field(TEXT_FIELD_CODE);
        long revision = appForGetFormLayoutPreview.getAppRevision(true);

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
        req.setApp(appForGetFormLayoutPreview.id());
        req.setLayout(layout);
        req.setRevision(revision);
        UpdateFormLayoutResponseBody resp = client.app().updateFormLayout(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);

        List<Layout> updatedLayout = appForGetFormLayoutPreview.getLayout(true);
        assertThat(updatedLayout).usingRecursiveFieldByFieldElementComparator().isEqualTo(layout);
    }
}
