package com.kintone.client.scenarios;

import com.kintone.client.KintoneClient;
import com.kintone.client.api.app.AddAppRequest;
import com.kintone.client.api.app.AddAppResponseBody;
import com.kintone.client.api.app.DeployAppRequest;
import com.kintone.client.api.app.GetDeployStatusRequest;
import com.kintone.client.api.app.GetDeployStatusResponseBody;
import com.kintone.client.api.app.UpdateAppSettingsRequest;
import com.kintone.client.api.common.DownloadFileRequest;
import com.kintone.client.api.common.DownloadFileResponseBody;
import com.kintone.client.api.common.UploadFileRequest;
import com.kintone.client.api.common.UploadFileResponseBody;
import com.kintone.client.model.FileBody;
import com.kintone.client.model.app.AppDeployStatus;
import com.kintone.client.model.app.AppPresetIcon;
import com.kintone.client.model.app.DeployApp;
import com.kintone.client.model.app.DeployStatus;
import com.kintone.client.model.app.field.Alignment;
import com.kintone.client.model.app.field.CalcFieldProperty;
import com.kintone.client.model.app.field.FieldMapping;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.app.field.FileFieldProperty;
import com.kintone.client.model.app.field.GroupFieldProperty;
import com.kintone.client.model.app.field.LookupFieldProperty;
import com.kintone.client.model.app.field.LookupSetting;
import com.kintone.client.model.app.field.NumberFieldProperty;
import com.kintone.client.model.app.field.Option;
import com.kintone.client.model.app.field.RadioButtonFieldProperty;
import com.kintone.client.model.app.field.ReferenceTable;
import com.kintone.client.model.app.field.ReferenceTableCondition;
import com.kintone.client.model.app.field.ReferenceTableFieldProperty;
import com.kintone.client.model.app.field.RelatedApp;
import com.kintone.client.model.app.field.SingleLineTextFieldProperty;
import com.kintone.client.model.app.layout.FieldLayout;
import com.kintone.client.model.app.layout.FieldSize;
import com.kintone.client.model.app.layout.GroupLayout;
import com.kintone.client.model.app.layout.Layout;
import com.kintone.client.model.app.layout.RowLayout;
import com.kintone.client.model.record.FieldType;
import com.kintone.client.model.record.FileFieldValue;
import com.kintone.client.model.record.Record;
import java.io.ByteArrayInputStream;
import java.util.*;

class ProductArrival {
    private final KintoneClient client;
    private final long productMasterApp;
    private long app;

    ProductArrival(KintoneClient client, long productMasterApp) {
        this.client = client;
        this.productMasterApp = productMasterApp;
    }

    long run() {
        installPreview();
        deploy();
        attachmentTest();
        return app;
    }

    private long installPreview() {
        newApp("Product Arrival");
        updateAppSettings();
        addFields();
        updateLayout();

        return app;
    }

    private void newApp(String name) {
        AddAppRequest request = new AddAppRequest();
        request.setName(name);
        AddAppResponseBody response = client.app().addApp(request);

        this.app = response.getApp();
    }

    private void updateAppSettings() {
        UpdateAppSettingsRequest req =
                new UpdateAppSettingsRequest()
                        .setApp(app)
                        .setName("Product Arrival" + "@" + new Date().toString())
                        .setDescription("product arrival")
                        .setIcon(new AppPresetIcon().setKey("APP84"))
                        .setTheme("RED");

        client.app().updateAppSettings(req);
    }

    private void addFields() {
        FieldProperty group =
                new GroupFieldProperty().setLabel("group").setOpenGroup(true).setCode("group");

        FieldProperty lookup =
                new LookupFieldProperty(FieldType.SINGLE_LINE_TEXT)
                        .setLabel("product code")
                        .setLookup(
                                new LookupSetting()
                                        .setRelatedApp(new RelatedApp().setApp(productMasterApp))
                                        .setRelatedKeyField("code")
                                        .setFieldMappings(
                                                Arrays.asList(
                                                        new FieldMapping().setField("productName").setRelatedField("name"),
                                                        new FieldMapping().setField("productPrice").setRelatedField("price")))
                                        .setLookupPickerFields(Arrays.asList("code", "name", "price"))
                                        .setFilterCond(null)
                                        .setSort(null))
                        .setCode("productCode");

        FieldProperty productName =
                new SingleLineTextFieldProperty().setLabel("Product name").setCode("productName");

        FieldProperty productPrice =
                new NumberFieldProperty().setLabel("Product price").setCode("productPrice");

        Map<String, Option> taxOptions = new HashMap<>();
        taxOptions.put("Yes", new Option().setLabel("Yes").setIndex(0L));
        taxOptions.put("No", new Option().setLabel("No").setIndex(1L));

        FieldProperty taxIncluded =
                new RadioButtonFieldProperty()
                        .setLabel("Tax included?")
                        .setOptions(taxOptions)
                        .setDefaultValue("No")
                        .setAlign(Alignment.VERTICAL)
                        .setCode("taxIncluded");

        FieldProperty count = new NumberFieldProperty().setLabel("Count of products").setCode("count");

        FieldProperty totalPrice =
                new CalcFieldProperty()
                        .setLabel("Total price")
                        .setExpression("productPrice * count * IF(taxIncluded = \"Yes\", 1.10, 1.0)")
                        .setCode("totalPrice");

        FieldProperty supplier =
                new SingleLineTextFieldProperty().setLabel("Supplier").setCode("supplier");

        FieldProperty attachment =
                new FileFieldProperty().setLabel("Attachment").setThumbnailSize(50L).setCode("attachment");

        FieldProperty related =
                new ReferenceTableFieldProperty()
                        .setReferenceTable(
                                new ReferenceTable()
                                        .setRelatedApp(new RelatedApp().setApp(app))
                                        .setCondition(
                                                new ReferenceTableCondition()
                                                        .setField("supplier")
                                                        .setRelatedField("supplier"))
                                        .setFilterCond(null)
                                        .setDisplayFields(Arrays.asList("productName", "count"))
                                        .setSort(null)
                                        .setSize(5L))
                        .setLabel("Related")
                        .setCode("related");

        Map<String, FieldProperty> m = new HashMap<>();

        m.put("group", group);
        m.put("productCode", lookup);
        m.put("productName", productName);
        m.put("productPrice", productPrice);
        m.put("taxIncluded", taxIncluded);
        m.put("count", count);
        m.put("totalPrice", totalPrice);
        m.put("supplier", supplier);
        m.put("attachment", attachment);
        m.put("related", related);

        client.app().addFormFields(app, m);
    }

    private void updateLayout() {
        FieldSize choudoyoiSize = new FieldSize().setWidth(300);

        GroupLayout group =
                new GroupLayout()
                        .setCode("group")
                        .setLayout(
                                Collections.singletonList(
                                        new RowLayout()
                                                .setFields(
                                                        Arrays.asList(
                                                                new FieldLayout()
                                                                        .setType(FieldType.SINGLE_LINE_TEXT)
                                                                        .setCode("productCode")
                                                                        .setSize(choudoyoiSize),
                                                                new FieldLayout()
                                                                        .setType(FieldType.SINGLE_LINE_TEXT)
                                                                        .setCode("productName")
                                                                        .setSize(choudoyoiSize),
                                                                new FieldLayout()
                                                                        .setType(FieldType.NUMBER)
                                                                        .setCode("productPrice")
                                                                        .setSize(choudoyoiSize)))));

        RowLayout price =
                new RowLayout()
                        .setFields(
                                Arrays.asList(
                                        new FieldLayout()
                                                .setType(FieldType.RADIO_BUTTON)
                                                .setCode("taxIncluded")
                                                .setSize(choudoyoiSize),
                                        new FieldLayout()
                                                .setType(FieldType.NUMBER)
                                                .setCode("count")
                                                .setSize(choudoyoiSize),
                                        new FieldLayout()
                                                .setType(FieldType.CALC)
                                                .setCode("totalPrice")
                                                .setSize(choudoyoiSize)));

        RowLayout supplier =
                new RowLayout()
                        .setFields(
                                Arrays.asList(
                                        new FieldLayout()
                                                .setType(FieldType.SINGLE_LINE_TEXT)
                                                .setCode("supplier")
                                                .setSize(choudoyoiSize),
                                        new FieldLayout()
                                                .setType(FieldType.FILE)
                                                .setCode("attachment")
                                                .setSize(choudoyoiSize)));

        RowLayout related =
                new RowLayout()
                        .setFields(
                                Collections.singletonList(
                                        new FieldLayout()
                                                .setType(FieldType.REFERENCE_TABLE)
                                                .setCode("related")
                                                .setSize(choudoyoiSize)));

        List<Layout> layout = Arrays.asList(group, price, supplier, related);
        client.app().updateFormLayout(app, layout);
    }

    private void deploy() {
        DeployAppRequest request = new DeployAppRequest();
        DeployApp deployApp = new DeployApp();
        deployApp.setApp(app);

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

    private void attachmentTest() {
        UploadFileRequest uploadFileRequest =
                new UploadFileRequest()
                        .setFilename("hoge.txt")
                        .setContentType("text/plain")
                        .setContent(new ByteArrayInputStream("<?php <<this is test>>>".getBytes()));
        UploadFileResponseBody response = client.file().uploadFile(uploadFileRequest);
        String key = response.getFileKey();

        Record record = new Record();
        record.putField("attachment", new FileFieldValue(new FileBody().setFileKey(key)));

        long id = client.record().addRecord(app, record);
        Record fetchedRecord = client.record().getRecord(app, id);

        DownloadFileRequest downloadFileRequest =
                new DownloadFileRequest()
                        .setFileKey(fetchedRecord.getFileFieldValue("attachment").get(0).getFileKey());

        DownloadFileResponseBody downloadFileResponseBody =
                client.file().downloadFile(downloadFileRequest);

        System.out.println(downloadFileResponseBody.getContentType());
        System.out.println(downloadFileResponseBody.getContentLength());
    }
}
