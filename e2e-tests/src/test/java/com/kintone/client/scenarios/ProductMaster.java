package com.kintone.client.scenarios;

import com.kintone.client.KintoneClient;
import com.kintone.client.Users;
import com.kintone.client.api.app.AddAppRequest;
import com.kintone.client.api.app.AddAppResponseBody;
import com.kintone.client.api.app.DeployAppRequest;
import com.kintone.client.api.app.GetDeployStatusRequest;
import com.kintone.client.api.app.GetDeployStatusResponseBody;
import com.kintone.client.api.app.UpdateAppSettingsRequest;
import com.kintone.client.api.app.UpdateAppSettingsResponseBody;
import com.kintone.client.api.app.UpdateProcessManagementRequest;
import com.kintone.client.api.app.UpdateProcessManagementResponseBody;
import com.kintone.client.api.app.UpdateViewsRequest;
import com.kintone.client.api.app.UpdateViewsResponseBody;
import com.kintone.client.api.common.UploadFileRequest;
import com.kintone.client.api.common.UploadFileResponseBody;
import com.kintone.client.api.record.CreateCursorRequest;
import com.kintone.client.api.record.CreateCursorResponseBody;
import com.kintone.client.api.record.DeleteCursorRequest;
import com.kintone.client.api.record.DeleteCursorResponseBody;
import com.kintone.client.api.record.GetRecordsByCursorRequest;
import com.kintone.client.api.record.GetRecordsByCursorResponseBody;
import com.kintone.client.api.record.UpdateRecordAssigneesRequest;
import com.kintone.client.api.record.UpdateRecordAssigneesResponseBody;
import com.kintone.client.api.record.UpdateRecordStatusRequest;
import com.kintone.client.api.record.UpdateRecordStatusesRequest;
import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import com.kintone.client.model.app.AppDeployStatus;
import com.kintone.client.model.app.AppPresetIcon;
import com.kintone.client.model.app.AppRightEntity;
import com.kintone.client.model.app.DeployApp;
import com.kintone.client.model.app.DeployStatus;
import com.kintone.client.model.app.ProcessAction;
import com.kintone.client.model.app.ProcessAssignee;
import com.kintone.client.model.app.ProcessAssigneeType;
import com.kintone.client.model.app.ProcessEntity;
import com.kintone.client.model.app.ProcessState;
import com.kintone.client.model.app.View;
import com.kintone.client.model.app.ViewType;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.app.field.MultiLineTextFieldProperty;
import com.kintone.client.model.app.field.NumberFieldProperty;
import com.kintone.client.model.app.field.RichTextFieldProperty;
import com.kintone.client.model.app.field.SingleLineTextFieldProperty;
import com.kintone.client.model.app.field.UnitPosition;
import com.kintone.client.model.app.layout.FieldLayout;
import com.kintone.client.model.app.layout.FieldSize;
import com.kintone.client.model.app.layout.Layout;
import com.kintone.client.model.app.layout.RowLayout;
import com.kintone.client.model.record.FieldType;
import com.kintone.client.model.record.NumberFieldValue;
import com.kintone.client.model.record.Record;
import com.kintone.client.model.record.RecordComment;
import com.kintone.client.model.record.RecordForUpdate;
import com.kintone.client.model.record.RichTextFieldValue;
import com.kintone.client.model.record.SingleLineTextFieldValue;
import com.kintone.client.model.record.StatusAction;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.*;

// 商品コード（文字列）、商品名（文字列）、単価（数値）、説明（リッチテキスト）を持つアプリ
// ユニットテスト的なことを目論んでいたので、test以下に配置した
class ProductMaster {
    private final KintoneClient client;
    private final String loginUserCode;
    private long app;
    private long revision;

    ProductMaster(KintoneClient client, String loginUser) {
        this.client = client;
        this.loginUserCode = loginUser;
    }

    long run() {
        installPreview();
        deploy();
        setupRecords();
        return app;
    }

    // region Installation Process
    // ====================================================================== //
    private long installPreview() {
        newApp("Product Master");
        updateAppSettings();
        addFields();
        updateFields();
        deleteFields();
        updateLayout();
        updateProcess();
        updateView();
        setupAppPermission();

        return app;
    }

    private void newApp(String name) {
        AddAppRequest request = new AddAppRequest();
        request.setName(name);
        AddAppResponseBody response = client.app().addApp(request);

        this.app = response.getApp();
        this.revision = response.getRevision();
    }

    private void updateAppSettings() {
        UpdateAppSettingsRequest req = new UpdateAppSettingsRequest();
        req.setApp(app);
        req.setName("Product Master" + "@" + new Date().toString());
        req.setDescription("product management app");

        AppPresetIcon icon = new AppPresetIcon();
        icon.setKey("APP86"); // telephone
        req.setIcon(icon);
        req.setTheme("RED");
        req.setRevision(revision);

        UpdateAppSettingsResponseBody resp = client.app().updateAppSettings(req);
        revision = resp.getRevision();
    }

    private void addFields() {
        SingleLineTextFieldProperty code = new SingleLineTextFieldProperty();
        code.setCode("code");
        code.setLabel("Product code");
        code.setNoLabel(false);
        code.setRequired(true);
        code.setUnique(true);
        code.setMaxLength(16L);
        code.setMinLength(1L);
        code.setDefaultValue(null);
        code.setExpression(null);
        code.setHideExpression(null);

        SingleLineTextFieldProperty name = new SingleLineTextFieldProperty();
        name.setCode("name");
        name.setLabel("Product name");
        name.setNoLabel(false);
        name.setRequired(false);
        name.setUnique(false);
        name.setMaxLength(100L);
        name.setMinLength(1L);
        name.setDefaultValue(null);
        name.setExpression(null);
        name.setHideExpression(null);

        NumberFieldProperty price = new NumberFieldProperty();
        price.setCode("price");
        price.setLabel("Price");
        price.setNoLabel(false);
        price.setRequired(true);
        price.setUnique(false);
        price.setMaxValue(BigDecimal.valueOf(100000000));
        price.setMinValue(BigDecimal.ONE);
        price.setDefaultValue(BigDecimal.TEN);
        price.setDigit(true);
        price.setDisplayScale(8L);
        price.setUnit("Yen");
        price.setUnitPosition(UnitPosition.BEFORE);

        RichTextFieldProperty description = new RichTextFieldProperty();
        description.setCode("description");
        description.setLabel("Product description");
        description.setNoLabel(true);
        description.setRequired(false);
        description.setDefaultValue("description here...");

        MultiLineTextFieldProperty memo = new MultiLineTextFieldProperty();
        memo.setCode("memo");
        memo.setLabel("Memo");

        Map<String, FieldProperty> m = new HashMap<>();
        m.put("code", code);
        m.put("name", name);
        m.put("price", price);
        m.put("description", description);
        m.put("memo", memo);

        revision = client.app().addFormFields(app, m, revision);
    }

    private void updateFields() {
        SingleLineTextFieldProperty code = new SingleLineTextFieldProperty();
        code.setLabel("商品コード");

        SingleLineTextFieldProperty name = new SingleLineTextFieldProperty();
        name.setLabel("商品名");

        Map<String, FieldProperty> m = new HashMap<>();
        m.put("code", code);
        m.put("name", name);

        revision = client.app().updateFormFields(app, m, revision);
    }

    private void deleteFields() {
        List<String> fields = new ArrayList<>();
        fields.add("memo");
        revision = client.app().deleteFormFields(app, fields);
    }

    private void updateLayout() {
        FieldLayout code = new FieldLayout();
        code.setCode("code");
        code.setType(FieldType.SINGLE_LINE_TEXT);
        code.setSize(new FieldSize().setWidth(120));

        FieldLayout name = new FieldLayout();
        name.setType(FieldType.SINGLE_LINE_TEXT);
        name.setCode("name");
        name.setSize(new FieldSize().setWidth(200));

        FieldLayout price = new FieldLayout();
        price.setType(FieldType.NUMBER);
        price.setCode("price");
        price.setSize(new FieldSize().setWidth(150));

        FieldLayout description = new FieldLayout();
        description.setType(FieldType.RICH_TEXT);
        description.setCode("description");
        description.setSize(new FieldSize().setHeight(400).setWidth(600));

        RowLayout codeAndName = new RowLayout();
        codeAndName.setFields(Arrays.asList(code, name));

        RowLayout priceRow = new RowLayout();
        priceRow.setFields(Collections.singletonList(price));

        RowLayout descriptionRow = new RowLayout();
        descriptionRow.setFields(Collections.singletonList(description));

        List<Layout> layout = Arrays.asList(codeAndName, priceRow, descriptionRow);
        revision = client.app().updateFormLayout(app, layout);
    }

    private void updateProcess() {
        UpdateProcessManagementRequest request = new UpdateProcessManagementRequest();
        request.setApp(app);
        request.setEnable(true);
        request.setRevision(revision);

        ProcessState registered = new ProcessState();

        registered.setName("Registered");
        registered.setIndex("0");

        ProcessState available = new ProcessState();
        available.setName("Available");
        available.setIndex("3");

        ProcessAssignee assigneeAvailable = new ProcessAssignee();
        assigneeAvailable.setType(ProcessAssigneeType.ALL);

        ProcessEntity processEntityAvailable = new ProcessEntity();
        processEntityAvailable.setEntity(new Entity(EntityType.USER, loginUserCode));

        assigneeAvailable.setEntities(Collections.singletonList(processEntityAvailable));
        available.setAssignee(assigneeAvailable);

        Map<String, ProcessState> states = new HashMap<>();
        states.put("Available", available);
        states.put("Registered", registered);
        request.setStates(states);

        ProcessAction confirm = new ProcessAction();
        confirm.setName("Confirm");
        confirm.setFrom("Registered");
        confirm.setTo("Available");

        request.setActions(Collections.singletonList(confirm));

        UpdateProcessManagementResponseBody response = client.app().updateProcessManagement(request);
        revision = response.getRevision();
    }

    private void updateView() {
        UpdateViewsRequest req = new UpdateViewsRequest();
        req.setApp(app);
        req.setRevision(revision);

        Map<String, View> views = client.app().getViewsPreview(app);
        View view = new View();
        view.setType(ViewType.LIST);
        view.setIndex(25L);
        view.setName("name and price");
        view.setFields(Arrays.asList("name", "price"));

        views.put("name and price", view);
        req.setViews(views);

        UpdateViewsResponseBody res = client.app().updateViews(req);
        revision = res.getRevision();
    }

    private void setupAppPermission() {
        AppRightEntity entity =
                new AppRightEntity()
                        .setEntity(new Entity(EntityType.USER, loginUserCode))
                        .setAppEditable(true)
                        .setRecordViewable(true)
                        .setRecordEditable(true)
                        .setRecordAddable(true)
                        .setRecordDeletable(true)
                        .setRecordImportable(true)
                        .setRecordExportable(true);

        List<AppRightEntity> rights = Collections.singletonList(entity);
        revision = client.app().updateAppAcl(app, rights, revision);
    }

    private void deploy() {
        DeployAppRequest request = new DeployAppRequest();
        DeployApp deployApp = new DeployApp();
        deployApp.setApp(app);
        deployApp.setRevision(revision);

        request.setApps(Collections.singletonList(deployApp));
        client.app().deployApp(request); // deploy のときはrevisionの更新は発生しない

        waitForDeploy();
    }

    private void waitForDeploy() {
        while (true) {
            GetDeployStatusRequest request = new GetDeployStatusRequest();
            request.setApps(Collections.singletonList(app));
            GetDeployStatusResponseBody response = client.app().getDeployStatus(request);

            AppDeployStatus status = response.getApps().get(0);
            if (status.getApp() == app && status.getStatus() != DeployStatus.PROCESSING) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    // ====================================================================== //
    // endregion Installation Process

    private Record createRecord(String code, String name, long price, String description) {
        Record record = new Record();
        record.putField("code", new SingleLineTextFieldValue(code));
        record.putField("name", new SingleLineTextFieldValue(name));
        record.putField("price", new NumberFieldValue(price));
        record.putField("description", new RichTextFieldValue(description));
        return record;
    }

    private void setupRecords() {
        // レコードの登録
        Record akahuku = createRecord("SOUVENIR-001", "赤福もち", 300, "<div>新大阪駅のお土産</div>");
        Record huroshikimanjuu = createRecord("SOUVENIR-002", "ふろしきまんじゅう", 400, "鳥取のお菓子");
        Record wakakusa = createRecord("SOUVENIR-003", "若草", 700, "山陰銘菓");

        // TODO: 作成者を指定して新規レコードを作るやつ

        long akahukuId = client.record().addRecord(app, akahuku);
        List<Long> saninIds = client.record().addRecords(app, Arrays.asList(huroshikimanjuu, wakakusa));

        // レコードの更新
        Record akahukuUpdate = new Record();
        akahukuUpdate.putField("name", new SingleLineTextFieldValue("赤福餅"));
        akahukuUpdate.putField("description", new RichTextFieldValue("<b>伊勢</b>のお土産"));
        client.record().updateRecord(app, akahukuId, akahukuUpdate);

        Long huroshikiId = saninIds.get(0);
        Long wakakusaId = saninIds.get(1);

        Record huroshikiUpdate = new Record();
        huroshikiUpdate.putField("price", new NumberFieldValue(432));
        RecordForUpdate huroshikiRFU = new RecordForUpdate(huroshikiId, huroshikiUpdate);

        Record wakakusaUpdate = new Record();
        wakakusaUpdate.putField("price", new NumberFieldValue(756));
        RecordForUpdate wakakusaRFU = new RecordForUpdate(wakakusaId, wakakusaUpdate);

        client.record().updateRecords(app, Arrays.asList(huroshikiRFU, wakakusaRFU));

        // レコードの取得のテスト
        //        {
        //            Record a = client.record().getRecord(app, akahukuId);
        //            List<Record> hs = client.record().getRecords(app, "price >= 400");
        //
        //            System.out.println(a.getSingleLineTextFieldValue("name"));
        //            for (Record h : hs) {
        //                System.out.println(h.getSingleLineTextFieldValue("name"));
        //            }
        //        }

        // 赤福を消す。他意はない
        client.record().deleteRecords(app, Collections.singletonList(akahukuId));

        RecordComment huroshikiComment =
                new RecordComment(
                        "山陰の空港とか土産物店でよく売っている",
                        Collections.singletonList(new Entity(EntityType.USER, Users.user1.getCode())));
        client.record().addRecordComment(app, huroshikiId, huroshikiComment);

        RecordComment huroshikiReply = new RecordComment("黒糖風味が効いていて美味しい");
        client.record().addRecordComment(app, huroshikiId, huroshikiReply);

        RecordComment wrongComment = new RecordComment("てすとてすと");
        long wrongCommentId = client.record().addRecordComment(app, huroshikiId, wrongComment);

        client.record().deleteRecordComment(app, huroshikiId, wrongCommentId);

        //        List<PostedRecordComment> comments =
        //                client.record().getRecordComments(app, huroshikiId, Order.ASC, 0L, 10L);
        //        for (PostedRecordComment comment : comments) {
        //            System.out.println(comment.getText());
        //        }

        // プロセス管理
        UpdateRecordAssigneesRequest assigneesRequest =
                new UpdateRecordAssigneesRequest()
                        .setApp(app)
                        .setId(wakakusaId)
                        .setAssignees(Collections.singletonList(loginUserCode));

        UpdateRecordAssigneesResponseBody updateRecordAssigneesResponseBody =
                client.record().updateRecordAssignees(assigneesRequest);

        UpdateRecordStatusRequest updateRecordStatusRequest =
                new UpdateRecordStatusRequest().setApp(app).setId(wakakusaId).setAction("Confirm");

        client.record().updateRecordStatus(updateRecordStatusRequest);

        UpdateRecordStatusesRequest updateRecordStatusesRequest =
                new UpdateRecordStatusesRequest()
                        .setApp(app)
                        .setRecords(
                                Collections.singletonList(
                                        new StatusAction().setId(huroshikiId).setAction("Confirm")));

        client.record().updateRecordStatuses(updateRecordStatusesRequest);
    }

    public void cursorTest() {
        CreateCursorRequest createCursorRequest = new CreateCursorRequest().setApp(app).setSize(1L);

        CreateCursorResponseBody createCursorResponseBody =
                client.record().createCursor(createCursorRequest);
        String id = createCursorResponseBody.getId();

        GetRecordsByCursorRequest getRecordsByCursorRequest = new GetRecordsByCursorRequest().setId(id);
        GetRecordsByCursorResponseBody getRecordsByCursorResponseBody =
                client.record().getRecordsByCursor(getRecordsByCursorRequest);

        //        for (Record record : getRecordsByCursorResponseBody.getRecords()) {
        //            System.out.println(record.getSingleLineTextFieldValue("name"));
        //        }

        DeleteCursorRequest deleteCursorRequest = new DeleteCursorRequest().setId(id);
        DeleteCursorResponseBody deleteCursorResponseBody =
                client.record().deleteCursor(deleteCursorRequest);
    }

    public void fileTest() {
        UploadFileRequest uploadFileRequest =
                new UploadFileRequest()
                        .setFilename("hoge.txt")
                        .setContentType("text/plain")
                        .setContent(new ByteArrayInputStream("<?php <<this is test>>>".getBytes()));
        UploadFileResponseBody response = client.file().uploadFile(uploadFileRequest);
        String key = response.getFileKey();

        //        DownloadFileRequest downloadFileRequest =
        //                new DownloadFileRequest().setFileKey(key);
        //
        //
        //        DownloadFileResponseBody downloadFileResponseBody =
        // client.file().downloadFile(downloadFileRequest);
        //
        //        System.out.println(downloadFileResponseBody.getContentType());
        //        System.out.println(downloadFileResponseBody.getContentLength());
    }
}
