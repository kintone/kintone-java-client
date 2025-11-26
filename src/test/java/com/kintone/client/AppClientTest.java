package com.kintone.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.api.app.*;
import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import com.kintone.client.model.User;
import com.kintone.client.model.app.ActionId;
import com.kintone.client.model.app.App;
import com.kintone.client.model.app.AppAction;
import com.kintone.client.model.app.AppDeployStatus;
import com.kintone.client.model.app.AppPlugin;
import com.kintone.client.model.app.AppRightEntity;
import com.kintone.client.model.app.CustomizeScope;
import com.kintone.client.model.app.DeployApp;
import com.kintone.client.model.app.DeployStatus;
import com.kintone.client.model.app.EvaluatedRecordRight;
import com.kintone.client.model.app.FieldAccessibility;
import com.kintone.client.model.app.FieldRight;
import com.kintone.client.model.app.FieldRightEntity;
import com.kintone.client.model.app.NumberPrecision;
import com.kintone.client.model.app.RecordRight;
import com.kintone.client.model.app.RecordRightEntity;
import com.kintone.client.model.app.TitleFieldSelectionMode;
import com.kintone.client.model.app.TitleFiled;
import com.kintone.client.model.app.View;
import com.kintone.client.model.app.ViewId;
import com.kintone.client.model.app.ViewType;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.app.field.SingleLineTextFieldProperty;
import com.kintone.client.model.app.layout.FieldLayout;
import com.kintone.client.model.app.layout.FieldSize;
import com.kintone.client.model.app.layout.Layout;
import com.kintone.client.model.app.layout.RowLayout;
import com.kintone.client.model.app.report.ChartType;
import com.kintone.client.model.app.report.Report;
import com.kintone.client.model.app.report.ReportId;
import com.kintone.client.model.record.FieldType;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

public class AppClientTest {

    private InternalClientMock mockClient = new InternalClientMock();
    private AppClient sut = new AppClient(mockClient, Collections.emptyList());

    private Map<String, FieldProperty> createTestProperty() {
        return Collections.singletonMap("text", createTestField());
    }

    private SingleLineTextFieldProperty createTestField() {
        SingleLineTextFieldProperty property = new SingleLineTextFieldProperty();
        property.setCode("text").setLabel("Text field");
        return property;
    }

    private List<Layout> createTestLayout() {
        return Arrays.asList(createTestRow("text1"), createTestRow("text2"));
    }

    private RowLayout createTestRow(String code) {
        FieldLayout field =
                new FieldLayout()
                        .setCode(code)
                        .setType(FieldType.SINGLE_LINE_TEXT)
                        .setSize(new FieldSize().setWidth(200));
        return new RowLayout().setFields(Collections.singletonList(field));
    }

    private Map<String, Report> createTestReports() {
        return Collections.singletonMap("report1", createTestReport());
    }

    private Report createTestReport() {
        return new Report().setId(100L).setChartType(ChartType.BAR);
    }

    private Map<String, View> createTestViews() {
        return Collections.singletonMap("view1", createTestView());
    }

    private View createTestView() {
        List<String> fields = Arrays.asList("text", "number");
        return new View().setId(100L).setType(ViewType.LIST).setFields(fields);
    }

    private App createTestApp() {
        ZonedDateTime time = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        User user = new User("User", "user");
        return new App(1L, "app", "App1", "", null, null, time, user, time, user);
    }

    private com.kintone.client.model.app.AppStatistics createTestAppStatistics() {
        ZonedDateTime time = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        User user = new User("User", "user");
        com.kintone.client.model.app.AppSpace space =
                new com.kintone.client.model.app.AppSpace(1L, "Test Space");
        return new com.kintone.client.model.app.AppStatistics(
                1L,
                "App1",
                space,
                "group1",
                com.kintone.client.model.app.AppStatisticsStatus.ACTIVATED,
                time,
                100L,
                10L,
                1000L,
                500L,
                true,
                user,
                time,
                user,
                time);
    }

    private List<AppRightEntity> createTestAppRightEntities() {
        return Collections.singletonList(createTestAppRightEntity());
    }

    private AppRightEntity createTestAppRightEntity() {
        return new AppRightEntity()
                .setEntity(new Entity(EntityType.USER, "user"))
                .setIncludeSubs(false)
                .setAppEditable(true)
                .setRecordAddable(false)
                .setRecordDeletable(true)
                .setRecordEditable(false)
                .setRecordExportable(true)
                .setRecordImportable(false)
                .setRecordViewable(true);
    }

    private List<RecordRight> createTestRecordRights() {
        return Collections.singletonList(createTestRecordRight());
    }

    private RecordRight createTestRecordRight() {
        RecordRightEntity entity =
                new RecordRightEntity()
                        .setEntity(new Entity(EntityType.USER, "user"))
                        .setIncludeSubs(false)
                        .setDeletable(true)
                        .setEditable(false)
                        .setViewable(true);
        return new RecordRight().setEntities(Collections.singletonList(entity)).setFilterCond("");
    }

    private List<FieldRight> createTestFieldRights() {
        return Collections.singletonList(createTestFieldRight());
    }

    private FieldRight createTestFieldRight() {
        FieldRightEntity entity =
                new FieldRightEntity()
                        .setEntity(new Entity(EntityType.USER, "user"))
                        .setIncludeSubs(false)
                        .setAccessibility(FieldAccessibility.READ);
        return new FieldRight().setCode("field1").setEntities(Collections.singletonList(entity));
    }

    @Test
    public void addApp_String() {
        mockClient.setResponseBody(new AddAppResponseBody(10L, 20L));

        assertThat(sut.addApp("app_name")).isEqualTo(10L);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_APP);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new AddAppRequest().setName("app_name").setSpace(null).setThread(null));
    }

    @Test
    public void addApp_String_Long_Long() {
        mockClient.setResponseBody(new AddAppResponseBody(8L, 13L));

        assertThat(sut.addApp("new_app", 40L, 60L)).isEqualTo(8L);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_APP);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new AddAppRequest().setName("new_app").setSpace(40L).setThread(60L));
    }

    @Test
    public void addApp_AddAppRequest() {
        AddAppRequest req = new AddAppRequest();
        AddAppResponseBody resp = new AddAppResponseBody(1, 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.addApp(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_APP);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void addFormFields_long_List() {
        mockClient.setResponseBody(new AddFormFieldsResponseBody(2));

        List<FieldProperty> props = Collections.singletonList(createTestField());
        assertThat(sut.addFormFields(1, props)).isEqualTo(2);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_FORM_FIELDS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new AddFormFieldsRequest()
                                .setApp(1L)
                                .setProperties(createTestProperty())
                                .setRevision(null));
    }

    @Test
    public void addFormFields_long_List_Long() {
        mockClient.setResponseBody(new AddFormFieldsResponseBody(3));

        List<FieldProperty> props = Collections.singletonList(createTestField());
        assertThat(sut.addFormFields(1, props, 2L)).isEqualTo(3);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_FORM_FIELDS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new AddFormFieldsRequest()
                                .setApp(1L)
                                .setProperties(createTestProperty())
                                .setRevision(2L));
    }

    @Test
    public void addFormFields_long_Map() {
        mockClient.setResponseBody(new AddFormFieldsResponseBody(2));

        assertThat(sut.addFormFields(1, createTestProperty())).isEqualTo(2);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_FORM_FIELDS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new AddFormFieldsRequest()
                                .setApp(1L)
                                .setProperties(createTestProperty())
                                .setRevision(null));
    }

    @Test
    public void addFormFields_long_Map_Long() {
        mockClient.setResponseBody(new AddFormFieldsResponseBody(3));

        assertThat(sut.addFormFields(1, createTestProperty(), 2L)).isEqualTo(3);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_FORM_FIELDS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new AddFormFieldsRequest()
                                .setApp(1L)
                                .setProperties(createTestProperty())
                                .setRevision(2L));
    }

    @Test
    public void addFormFields_AddFormFieldsRequest() {
        AddFormFieldsRequest req = new AddFormFieldsRequest();
        AddFormFieldsResponseBody resp = new AddFormFieldsResponseBody(1);
        mockClient.setResponseBody(resp);

        assertThat(sut.addFormFields(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_FORM_FIELDS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void addPlugins_long_List() {
        mockClient.setResponseBody(new AddAppPluginsResponseBody(2));

        assertThat(sut.addPlugins(1, Collections.singletonList("plugin1"))).isEqualTo(2);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_APP_PLUGINS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new AddAppPluginsRequest()
                                .setApp(1L)
                                .setIds(Collections.singletonList("plugin1"))
                                .setRevision(null));
    }

    @Test
    public void addPlugins_AddAppPluginsRequest() {
        AddAppPluginsRequest req = new AddAppPluginsRequest();
        AddAppPluginsResponseBody resp = new AddAppPluginsResponseBody(1);
        mockClient.setResponseBody(resp);

        assertThat(sut.addPlugins(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.ADD_APP_PLUGINS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void deleteFormFields_long_List() {
        mockClient.setResponseBody(new DeleteFormFieldsResponseBody(2));

        assertThat(sut.deleteFormFields(1, Collections.singletonList("text"))).isEqualTo(2);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DELETE_FORM_FIELDS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new DeleteFormFieldsRequest()
                                .setApp(1L)
                                .setFields(Collections.singletonList("text"))
                                .setRevision(null));
    }

    @Test
    public void deleteFormFields_long_List_Long() {
        mockClient.setResponseBody(new DeleteFormFieldsResponseBody(3));

        assertThat(sut.deleteFormFields(1, Collections.singletonList("text"), 2L)).isEqualTo(3);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DELETE_FORM_FIELDS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new DeleteFormFieldsRequest()
                                .setApp(1L)
                                .setFields(Collections.singletonList("text"))
                                .setRevision(2L));
    }

    @Test
    public void deleteFormFields_DeleteFormFieldsRequest() {
        DeleteFormFieldsRequest req = new DeleteFormFieldsRequest();
        DeleteFormFieldsResponseBody resp = new DeleteFormFieldsResponseBody(1);
        mockClient.setResponseBody(resp);

        assertThat(sut.deleteFormFields(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DELETE_FORM_FIELDS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void deployApp_long() {
        mockClient.setResponseBody(new DeployAppResponseBody());

        sut.deployApp(1);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DEPLOY_APP);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new DeployAppRequest()
                                .setApps(Collections.singletonList(new DeployApp().setApp(1L).setRevision(null)))
                                .setRevert(false));
    }

    @Test
    public void deployApp_long_Long() {
        mockClient.setResponseBody(new DeployAppResponseBody());

        sut.deployApp(1, 2L);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DEPLOY_APP);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new DeployAppRequest()
                                .setApps(Collections.singletonList(new DeployApp().setApp(1L).setRevision(2L)))
                                .setRevert(false));
    }

    @Test
    public void deployApp_DeployAppRequest() {
        DeployAppRequest req = new DeployAppRequest();
        DeployAppResponseBody resp = new DeployAppResponseBody();
        mockClient.setResponseBody(resp);

        assertThat(sut.deployApp(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DEPLOY_APP);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void evaluateRecordAcl_long_long() {
        EvaluatedRecordRight right = new EvaluatedRecordRight(10L, null, null);
        EvaluateRecordAclResponseBody resp =
                new EvaluateRecordAclResponseBody(Lists.newArrayList(right));
        mockClient.setResponseBody(resp);

        assertThat(sut.evaluateRecordAcl(10L, 20L)).isEqualTo(right);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.EVALUATE_RECORD_ACL);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new EvaluateRecordAclRequest().setApp(10L).setIds(Lists.newArrayList(20L)));
    }

    @Test
    public void evaluateRecordAcl_long_List() {
        EvaluatedRecordRight right = new EvaluatedRecordRight(10L, null, null);
        EvaluateRecordAclResponseBody resp =
                new EvaluateRecordAclResponseBody(Lists.newArrayList(right));
        mockClient.setResponseBody(resp);

        assertThat(sut.evaluateRecordAcl(10L, Lists.newArrayList(30L)))
                .isEqualTo(Lists.newArrayList(right));
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.EVALUATE_RECORD_ACL);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new EvaluateRecordAclRequest().setApp(10L).setIds(Lists.newArrayList(30L)));
    }

    @Test
    public void evaluateRecordAcl_EvaluateRecordAclRequest() {
        EvaluateRecordAclRequest req = new EvaluateRecordAclRequest();
        EvaluateRecordAclResponseBody resp = new EvaluateRecordAclResponseBody(Collections.emptyList());
        mockClient.setResponseBody(resp);

        assertThat(sut.evaluateRecordAcl(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.EVALUATE_RECORD_ACL);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getAdminNotes_long() {
        GetAdminNotesResponseBody resp = new GetAdminNotesResponseBody("test", true, 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAdminNotes(1)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_ADMIN_NOTES);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetAdminNotesRequest().setApp(1L));
    }

    @Test
    public void getAdminNotes_GetAdminNotesRequest() {
        GetAdminNotesRequest req = new GetAdminNotesRequest();
        GetAdminNotesResponseBody resp = new GetAdminNotesResponseBody("test", true, 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAdminNotes(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_ADMIN_NOTES);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getAdminNotesPreview_long() {
        GetAdminNotesPreviewResponseBody resp = new GetAdminNotesPreviewResponseBody("test", true, 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAdminNotesPreview(1)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_ADMIN_NOTES_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetAdminNotesPreviewRequest().setApp(1L));
    }

    @Test
    public void getAdminNotesPreview_GetAdminNotesPreviewRequest() {
        GetAdminNotesPreviewRequest req = new GetAdminNotesPreviewRequest();
        GetAdminNotesPreviewResponseBody resp = new GetAdminNotesPreviewResponseBody("test", true, 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAdminNotesPreview(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_ADMIN_NOTES_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getApp_long() {
        ZonedDateTime time = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        User user = new User("User", "user");
        mockClient.setResponseBody(
                new GetAppResponseBody(1L, "app", "App1", "", null, null, time, user, time, user));

        assertThat(sut.getApp(1)).isEqualTo(createTestApp());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetAppRequest().setId(1L));
    }

    @Test
    public void getApp_GetAppRequest() {
        GetAppRequest req = new GetAppRequest();
        GetAppResponseBody resp =
                new GetAppResponseBody(1, "", "", "", null, null, null, null, null, null);
        mockClient.setResponseBody(resp);

        assertThat(sut.getApp(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getAppAcl_long() {
        mockClient.setResponseBody(new GetAppAclResponseBody(createTestAppRightEntities(), 2));

        assertThat(sut.getAppAcl(1)).containsExactly(createTestAppRightEntity());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_ACL);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetAppAclRequest().setApp(1L));
    }

    @Test
    public void getAppAcl_GetAppAclRequest() {
        GetAppAclRequest req = new GetAppAclRequest();
        GetAppAclResponseBody resp = new GetAppAclResponseBody(Collections.emptyList(), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAppAcl(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_ACL);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getAppAclPreview_long() {
        mockClient.setResponseBody(new GetAppAclPreviewResponseBody(createTestAppRightEntities(), 2));

        assertThat(sut.getAppAclPreview(1)).containsExactly(createTestAppRightEntity());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_ACL_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetAppAclPreviewRequest().setApp(1L));
    }

    @Test
    public void getAppAclPreview_GetAppAclPreviewRequest() {
        GetAppAclPreviewRequest req = new GetAppAclPreviewRequest();
        GetAppAclPreviewResponseBody resp =
                new GetAppAclPreviewResponseBody(Collections.emptyList(), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAppAclPreview(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_ACL_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getAppActions_long() {
        Map<String, AppAction> actions = Collections.singletonMap("action1", new AppAction());
        GetAppActionsResponseBody resp = new GetAppActionsResponseBody(actions, 2L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAppActions(1L)).isEqualTo(actions);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_ACTIONS);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetAppActionsRequest().setApp(1L));
    }

    @Test
    public void getAppActions_long_String() {
        Map<String, AppAction> actions = Collections.singletonMap("action1", new AppAction());
        GetAppActionsResponseBody resp = new GetAppActionsResponseBody(actions, 2L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAppActions(1L, "en")).isEqualTo(actions);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_ACTIONS);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetAppActionsRequest().setApp(1L).setLang("en"));
    }

    @Test
    public void getAppActions_GetAppActionsRequest() {
        GetAppActionsRequest req = new GetAppActionsRequest();
        GetAppActionsResponseBody resp = new GetAppActionsResponseBody(null, 2L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAppActions(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_ACTIONS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getAppActionsPreview_long() {
        Map<String, AppAction> actions = Collections.singletonMap("action1", new AppAction());
        GetAppActionsPreviewResponseBody resp = new GetAppActionsPreviewResponseBody(actions, 2L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAppActionsPreview(1L)).isEqualTo(actions);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_ACTIONS_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetAppActionsPreviewRequest().setApp(1L));
    }

    @Test
    public void getAppActionsPreview_long_String() {
        Map<String, AppAction> actions = Collections.singletonMap("action1", new AppAction());
        GetAppActionsPreviewResponseBody resp = new GetAppActionsPreviewResponseBody(actions, 2L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAppActionsPreview(1L, "en")).isEqualTo(actions);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_ACTIONS_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetAppActionsPreviewRequest().setApp(1L).setLang("en"));
    }

    @Test
    public void getAppActionsPreview_GetAppActionsPreviewRequest() {
        GetAppActionsPreviewRequest req = new GetAppActionsPreviewRequest();
        GetAppActionsPreviewResponseBody resp = new GetAppActionsPreviewResponseBody(null, 2L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAppActionsPreview(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_ACTIONS_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getAppCustomize_long() {
        GetAppCustomizeResponseBody resp =
                new GetAppCustomizeResponseBody(CustomizeScope.ADMIN, null, null, 13L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAppCustomize(7L)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_CUSTOMIZE);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetAppCustomizeRequest().setApp(7L));
    }

    @Test
    public void getAppCustomize_GetAppCustomizeRequest() {
        GetAppCustomizeRequest req = new GetAppCustomizeRequest();
        GetAppCustomizeResponseBody resp = new GetAppCustomizeResponseBody(null, null, null, 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAppCustomize(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_CUSTOMIZE);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getAppCustomizePreview_long() {
        GetAppCustomizePreviewResponseBody resp =
                new GetAppCustomizePreviewResponseBody(CustomizeScope.ADMIN, null, null, 13L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAppCustomizePreview(14L)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_CUSTOMIZE_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetAppCustomizePreviewRequest().setApp(14L));
    }

    @Test
    public void getAppCustomizePreview_GetAppCustomizePreviewRequest() {
        GetAppCustomizePreviewRequest req = new GetAppCustomizePreviewRequest();
        GetAppCustomizePreviewResponseBody resp =
                new GetAppCustomizePreviewResponseBody(null, null, null, 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAppCustomizePreview(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_CUSTOMIZE_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getApps_GetAppsRequest() {
        GetAppsRequest req = new GetAppsRequest();
        GetAppsResponseBody resp = new GetAppsResponseBody(Collections.emptyList());
        mockClient.setResponseBody(resp);

        assertThat(sut.getApps(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APPS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getAppsByCodes_List() {
        mockClient.setResponseBody(new GetAppsResponseBody(Collections.singletonList(createTestApp())));

        assertThat(sut.getAppsByCodes(Arrays.asList("app1", "app2"))).containsExactly(createTestApp());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APPS);
        assertThat(mockClient.getLastBody())
                .isEqualTo(
                        new GetAppsRequest()
                                .setCodes(Arrays.asList("app1", "app2"))
                                .setIds(null)
                                .setOffset(null)
                                .setLimit(null)
                                .setName(null)
                                .setSpaceIds(null));
    }

    @Test
    public void getAppsByIds_List() {
        mockClient.setResponseBody(new GetAppsResponseBody(Collections.singletonList(createTestApp())));

        assertThat(sut.getAppsByIds(Arrays.asList(1L, 2L))).containsExactly(createTestApp());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APPS);
        assertThat(mockClient.getLastBody())
                .isEqualTo(
                        new GetAppsRequest()
                                .setCodes(null)
                                .setIds(Arrays.asList(1L, 2L))
                                .setOffset(null)
                                .setLimit(null)
                                .setName(null)
                                .setSpaceIds(null));
    }

    @Test
    public void getStatistics_GetAppsStatisticsRequest() {
        GetAppsStatisticsRequest req = new GetAppsStatisticsRequest();
        GetAppsStatisticsResponseBody resp = new GetAppsStatisticsResponseBody(Collections.emptyList());
        mockClient.setResponseBody(resp);

        assertThat(sut.getStatistics(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APPS_STATISTICS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getStatistics_noArg() {
        mockClient.setResponseBody(
                new GetAppsStatisticsResponseBody(Collections.singletonList(createTestAppStatistics())));

        assertThat(sut.getStatistics()).containsExactly(createTestAppStatistics());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APPS_STATISTICS);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetAppsStatisticsRequest().setOffset(null).setLimit(null));
    }

    @Test
    public void getAppSettings_long() {
        GetAppSettingsResponseBody resp =
                new GetAppSettingsResponseBody(
                        "name",
                        "desc",
                        null,
                        null,
                        new TitleFiled().setSelectionMode(TitleFieldSelectionMode.AUTO),
                        new NumberPrecision().setDigits(40).setDecimalPlaces(10),
                        4,
                        true,
                        true,
                        true,
                        true,
                        true,
                        10L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAppSettings(10L)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_SETTINGS);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetAppSettingsRequest().setApp(10L).setLang(null));
    }

    @Test
    public void getAppSettings_long_String() {
        GetAppSettingsResponseBody resp =
                new GetAppSettingsResponseBody(
                        "name",
                        "desc",
                        null,
                        null,
                        new TitleFiled().setSelectionMode(TitleFieldSelectionMode.AUTO),
                        new NumberPrecision().setDigits(40).setDecimalPlaces(10),
                        4,
                        true,
                        true,
                        true,
                        true,
                        true,
                        10L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAppSettings(10L, "ja")).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_SETTINGS);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetAppSettingsRequest().setApp(10L).setLang("ja"));
    }

    @Test
    public void getAppSettings_GetAppSettingsRequest() {
        GetAppSettingsRequest req = new GetAppSettingsRequest();
        GetAppSettingsResponseBody resp =
                new GetAppSettingsResponseBody(
                        "",
                        "",
                        null,
                        "",
                        new TitleFiled().setSelectionMode(TitleFieldSelectionMode.AUTO),
                        new NumberPrecision().setDigits(40).setDecimalPlaces(10),
                        4,
                        true,
                        true,
                        true,
                        true,
                        true,
                        1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAppSettings(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_SETTINGS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getAppSettingsPreview_long() {
        GetAppSettingsPreviewResponseBody resp =
                new GetAppSettingsPreviewResponseBody(
                        "name",
                        "desc",
                        null,
                        null,
                        new TitleFiled().setSelectionMode(TitleFieldSelectionMode.AUTO),
                        new NumberPrecision().setDigits(40).setDecimalPlaces(10),
                        4,
                        true,
                        true,
                        true,
                        true,
                        true,
                        10L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAppSettingsPreview(10L)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_SETTINGS_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetAppSettingsPreviewRequest().setApp(10L).setLang(null));
    }

    @Test
    public void getAppSettingsPreview_long_String() {
        GetAppSettingsPreviewResponseBody resp =
                new GetAppSettingsPreviewResponseBody(
                        "name",
                        "desc",
                        null,
                        null,
                        new TitleFiled().setSelectionMode(TitleFieldSelectionMode.AUTO),
                        new NumberPrecision().setDigits(40).setDecimalPlaces(10),
                        4,
                        true,
                        true,
                        true,
                        true,
                        true,
                        10L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAppSettingsPreview(10L, "ja")).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_SETTINGS_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetAppSettingsPreviewRequest().setApp(10L).setLang("ja"));
    }

    @Test
    public void getAppSettingsPreview_GetAppSettingsPreviewRequest() {
        GetAppSettingsPreviewRequest req = new GetAppSettingsPreviewRequest();
        GetAppSettingsPreviewResponseBody resp =
                new GetAppSettingsPreviewResponseBody(
                        "",
                        "",
                        null,
                        "",
                        new TitleFiled().setSelectionMode(TitleFieldSelectionMode.AUTO),
                        new NumberPrecision().setDigits(40).setDecimalPlaces(10),
                        4,
                        true,
                        true,
                        true,
                        true,
                        true,
                        1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getAppSettingsPreview(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_SETTINGS_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getDeployStatus_long() {
        GetDeployStatusRequest req = new GetDeployStatusRequest();
        req.setApps(Lists.newArrayList(10L));

        AppDeployStatus status = new AppDeployStatus(3L, DeployStatus.PROCESSING);
        GetDeployStatusResponseBody resp = new GetDeployStatusResponseBody(Lists.newArrayList(status));
        mockClient.setResponseBody(resp);

        assertThat(sut.getDeployStatus(10L)).isEqualTo(DeployStatus.PROCESSING);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_DEPLOY_STATUS);
        assertThat(mockClient.getLastBody()).usingRecursiveComparison().isEqualTo(req);
    }

    @Test
    public void getDeployStatus_GetDeployStatusRequest() {
        GetDeployStatusRequest req = new GetDeployStatusRequest();
        GetDeployStatusResponseBody resp = new GetDeployStatusResponseBody(Collections.emptyList());
        mockClient.setResponseBody(resp);

        assertThat(sut.getDeployStatus(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_DEPLOY_STATUS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getFieldAcl_long() {
        mockClient.setResponseBody(new GetFieldAclResponseBody(createTestFieldRights(), 2));

        assertThat(sut.getFieldAcl(1)).containsExactly(createTestFieldRight());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_FIELD_ACL);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetFieldAclRequest().setApp(1L));
    }

    @Test
    public void getFieldAcl_GetFieldAclRequest() {
        GetFieldAclRequest req = new GetFieldAclRequest();
        GetFieldAclResponseBody resp = new GetFieldAclResponseBody(Collections.emptyList(), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getFieldAcl(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_FIELD_ACL);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getFieldAclPreview_long() {
        mockClient.setResponseBody(new GetFieldAclPreviewResponseBody(createTestFieldRights(), 2));

        assertThat(sut.getFieldAclPreview(1)).containsExactly(createTestFieldRight());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_FIELD_ACL_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetFieldAclPreviewRequest().setApp(1L));
    }

    @Test
    public void getFieldAclPreview_GetFieldAclPreviewRequest() {
        GetFieldAclPreviewRequest req = new GetFieldAclPreviewRequest();
        GetFieldAclPreviewResponseBody resp =
                new GetFieldAclPreviewResponseBody(Collections.emptyList(), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getFieldAclPreview(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_FIELD_ACL_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getFormFields_long() {
        mockClient.setResponseBody(new GetFormFieldsResponseBody(createTestProperty(), 2));

        Map<String, FieldProperty> result = sut.getFormFields(1);
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("text", createTestField());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_FORM_FIELDS);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetFormFieldsRequest().setApp(1L).setLang(null));
    }

    @Test
    public void getFormFields_long_String() {
        mockClient.setResponseBody(new GetFormFieldsResponseBody(createTestProperty(), 2));

        Map<String, FieldProperty> result = sut.getFormFields(1, "en");
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("text", createTestField());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_FORM_FIELDS);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetFormFieldsRequest().setApp(1L).setLang("en"));
    }

    @Test
    public void getFormFields_GetFormFieldsRequest() {
        GetFormFieldsRequest req = new GetFormFieldsRequest();
        GetFormFieldsResponseBody resp = new GetFormFieldsResponseBody(Collections.emptyMap(), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getFormFields(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_FORM_FIELDS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getFormFieldsPreview_long() {
        mockClient.setResponseBody(new GetFormFieldsPreviewResponseBody(createTestProperty(), 2));

        Map<String, FieldProperty> result = sut.getFormFieldsPreview(1);
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("text", createTestField());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_FORM_FIELDS_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetFormFieldsPreviewRequest().setApp(1L).setLang(null));
    }

    @Test
    public void getFormFieldsPreview_long_String() {
        mockClient.setResponseBody(new GetFormFieldsPreviewResponseBody(createTestProperty(), 2));

        Map<String, FieldProperty> result = sut.getFormFieldsPreview(1, "en");
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("text", createTestField());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_FORM_FIELDS_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetFormFieldsPreviewRequest().setApp(1L).setLang("en"));
    }

    @Test
    public void getFormFieldsPreview_GetFormFieldsPreviewRequest() {
        GetFormFieldsPreviewRequest req = new GetFormFieldsPreviewRequest();
        GetFormFieldsPreviewResponseBody resp =
                new GetFormFieldsPreviewResponseBody(Collections.emptyMap(), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getFormFieldsPreview(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_FORM_FIELDS_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getFormLayout_long() {
        mockClient.setResponseBody(new GetFormLayoutResponseBody(createTestLayout(), 2));

        List<Layout> result = sut.getFormLayout(1);
        assertThat(result).containsExactly(createTestRow("text1"), createTestRow("text2"));
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_FORM_LAYOUT);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetFormLayoutRequest().setApp(1L));
    }

    @Test
    public void getFormLayout_GetLayoutRequest() {
        GetFormLayoutRequest req = new GetFormLayoutRequest();
        GetFormLayoutResponseBody resp = new GetFormLayoutResponseBody(Collections.emptyList(), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getFormLayout(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_FORM_LAYOUT);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getFormLayoutPreview_long() {
        mockClient.setResponseBody(new GetFormLayoutPreviewResponseBody(createTestLayout(), 2));

        List<Layout> result = sut.getFormLayoutPreview(1);
        assertThat(result).containsExactly(createTestRow("text1"), createTestRow("text2"));
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_FORM_LAYOUT_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetFormLayoutPreviewRequest().setApp(1L));
    }

    @Test
    public void getFormLayoutPreview_GetLayoutPreviewRequest() {
        GetFormLayoutPreviewRequest req = new GetFormLayoutPreviewRequest();
        GetFormLayoutPreviewResponseBody resp =
                new GetFormLayoutPreviewResponseBody(Collections.emptyList(), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getFormLayoutPreview(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_FORM_LAYOUT_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getGeneralNotifications_long() {
        GetGeneralNotificationsResponseBody resp =
                new GetGeneralNotificationsResponseBody(null, true, 1L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getGeneralNotifications(10L)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_GENERAL_NOTIFICATIONS);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetGeneralNotificationsRequest().setApp(10L));
    }

    @Test
    public void getGeneralNotifications_GetGeneralNotificationsRequest() {
        GetGeneralNotificationsRequest req = new GetGeneralNotificationsRequest().setApp(10L);
        GetGeneralNotificationsResponseBody resp =
                new GetGeneralNotificationsResponseBody(null, true, 1L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getGeneralNotifications(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_GENERAL_NOTIFICATIONS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getGeneralNotificationsPreview_long() {
        GetGeneralNotificationsPreviewResponseBody resp =
                new GetGeneralNotificationsPreviewResponseBody(null, true, 1L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getGeneralNotificationsPreview(10L)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_GENERAL_NOTIFICATIONS_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetGeneralNotificationsPreviewRequest().setApp(10L));
    }

    @Test
    public void getGeneralNotificationsPreview_GetGeneralNotificationsPreviewRequest() {
        GetGeneralNotificationsPreviewRequest req =
                new GetGeneralNotificationsPreviewRequest().setApp(10L);
        GetGeneralNotificationsPreviewResponseBody resp =
                new GetGeneralNotificationsPreviewResponseBody(null, true, 1L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getGeneralNotificationsPreview(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_GENERAL_NOTIFICATIONS_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getPerRecordNotifications_long() {
        GetPerRecordNotificationsResponseBody resp =
                new GetPerRecordNotificationsResponseBody(null, 1L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getPerRecordNotifications(10L)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_PRE_RECORD_NOTIFICATIONS);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetPerRecordNotificationsRequest().setApp(10L));
    }

    @Test
    public void getPerRecordNotifications_long_String() {
        GetPerRecordNotificationsResponseBody resp =
                new GetPerRecordNotificationsResponseBody(null, 1L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getPerRecordNotifications(10L, "en")).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_PRE_RECORD_NOTIFICATIONS);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetPerRecordNotificationsRequest().setApp(10L).setLang("en"));
    }

    @Test
    public void getPerRecordNotifications_GetPerRecordNotificationsRequest() {
        GetPerRecordNotificationsRequest req = new GetPerRecordNotificationsRequest().setApp(10L);
        GetPerRecordNotificationsResponseBody resp =
                new GetPerRecordNotificationsResponseBody(null, 1L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getPerRecordNotifications(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_PRE_RECORD_NOTIFICATIONS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getPerRecordNotificationsPreview_long() {
        GetPerRecordNotificationsPreviewResponseBody resp =
                new GetPerRecordNotificationsPreviewResponseBody(null, 1L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getPerRecordNotificationsPreview(10L)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_PRE_RECORD_NOTIFICATIONS_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetPerRecordNotificationsPreviewRequest().setApp(10L));
    }

    @Test
    public void getPerRecordNotificationsPreview_long_String() {
        GetPerRecordNotificationsPreviewResponseBody resp =
                new GetPerRecordNotificationsPreviewResponseBody(null, 1L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getPerRecordNotificationsPreview(10L, "en")).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_PRE_RECORD_NOTIFICATIONS_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetPerRecordNotificationsPreviewRequest().setApp(10L).setLang("en"));
    }

    @Test
    public void getPerRecordNotificationsPreview_GetPerRecordNotificationsPreviewRequest() {
        GetPerRecordNotificationsPreviewRequest req =
                new GetPerRecordNotificationsPreviewRequest().setApp(10L);
        GetPerRecordNotificationsPreviewResponseBody resp =
                new GetPerRecordNotificationsPreviewResponseBody(null, 1L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getPerRecordNotificationsPreview(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_PRE_RECORD_NOTIFICATIONS_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getPlugins_long() {
        AppPlugin appPlugin = new AppPlugin("pluginId", "pluginName", true);

        GetAppPluginsResponseBody resp =
                new GetAppPluginsResponseBody(Collections.singletonList(appPlugin), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getPlugins(10L)).containsExactly(appPlugin);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_PLUGINS);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetAppPluginsRequest().setApp(10L).setLang(null));
    }

    @Test
    public void getPlugins_long_String() {
        AppPlugin appPlugin = new AppPlugin("pluginId", "pluginName", true);

        GetAppPluginsResponseBody resp =
                new GetAppPluginsResponseBody(Collections.singletonList(appPlugin), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getPlugins(10L, "en")).containsExactly(appPlugin);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_PLUGINS);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetAppPluginsRequest().setApp(10L).setLang("en"));
    }

    @Test
    public void getPlugins_GetPluginsRequest() {
        GetAppPluginsRequest req = new GetAppPluginsRequest();
        req.setApp(10L);
        req.setLang("en");

        AppPlugin appPlugin = new AppPlugin("pluginId", "pluginName", true);
        GetAppPluginsResponseBody resp =
                new GetAppPluginsResponseBody(Collections.singletonList(appPlugin), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getPlugins(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_PLUGINS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getPluginsPreview_long() {
        AppPlugin appPlugin = new AppPlugin("pluginId", "pluginName", true);

        GetAppPluginsPreviewResponseBody resp =
                new GetAppPluginsPreviewResponseBody(Collections.singletonList(appPlugin), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getPluginsPreview(10L)).containsExactly(appPlugin);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_PLUGINS_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetAppPluginsPreviewRequest().setApp(10L).setLang(null));
    }

    @Test
    public void getPluginsPreview_long_String() {
        AppPlugin appPlugin = new AppPlugin("pluginId", "pluginName", true);

        GetAppPluginsPreviewResponseBody resp =
                new GetAppPluginsPreviewResponseBody(Collections.singletonList(appPlugin), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getPluginsPreview(10L, "en")).containsExactly(appPlugin);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_PLUGINS_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetAppPluginsPreviewRequest().setApp(10L).setLang("en"));
    }

    @Test
    public void getPlugins_GetPluginsPreviewRequest() {
        GetAppPluginsPreviewRequest req = new GetAppPluginsPreviewRequest();
        req.setApp(10L);
        req.setLang("en");

        AppPlugin appPlugin = new AppPlugin("pluginId", "pluginName", true);
        GetAppPluginsPreviewResponseBody resp =
                new GetAppPluginsPreviewResponseBody(Collections.singletonList(appPlugin), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getPluginsPreview(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APP_PLUGINS_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getProcessManagement_long() {
        GetProcessManagementResponseBody resp =
                new GetProcessManagementResponseBody(true, null, null, 3L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getProcessManagement(10L)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_PROCESS_MANAGEMENT);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetProcessManagementRequest().setApp(10L).setLang(null));
    }

    @Test
    public void getProcessManagement_long_String() {
        GetProcessManagementResponseBody resp =
                new GetProcessManagementResponseBody(false, null, null, 3L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getProcessManagement(20L, "en")).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_PROCESS_MANAGEMENT);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetProcessManagementRequest().setApp(20L).setLang("en"));
    }

    @Test
    public void getProcessManagement_GetProcessManagementRequest() {
        GetProcessManagementRequest req = new GetProcessManagementRequest();
        GetProcessManagementResponseBody resp =
                new GetProcessManagementResponseBody(
                        true, Collections.emptyMap(), Collections.emptyList(), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getProcessManagement(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_PROCESS_MANAGEMENT);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getProcessManagementPreview_long() {
        GetProcessManagementPreviewResponseBody resp =
                new GetProcessManagementPreviewResponseBody(true, null, null, 5L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getProcessManagementPreview(30L)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_PROCESS_MANAGEMENT_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetProcessManagementPreviewRequest().setApp(30L).setLang(null));
    }

    @Test
    public void getProcessManagementPreview_long_String() {
        GetProcessManagementPreviewResponseBody resp =
                new GetProcessManagementPreviewResponseBody(false, null, null, 3L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getProcessManagementPreview(40L, "ja")).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_PROCESS_MANAGEMENT_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetProcessManagementPreviewRequest().setApp(40L).setLang("ja"));
    }

    @Test
    public void getProcessManagementPreview_GetProcessManagementPreviewRequest() {
        GetProcessManagementPreviewRequest req = new GetProcessManagementPreviewRequest();
        GetProcessManagementPreviewResponseBody resp =
                new GetProcessManagementPreviewResponseBody(
                        true, Collections.emptyMap(), Collections.emptyList(), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getProcessManagementPreview(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_PROCESS_MANAGEMENT_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getRecordAcl_long() {
        mockClient.setResponseBody(new GetRecordAclResponseBody(createTestRecordRights(), 2));

        assertThat(sut.getRecordAcl(1)).containsExactly(createTestRecordRight());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORD_ACL);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetRecordAclRequest().setApp(1L).setLang(null));
    }

    @Test
    public void getRecordAcl_long_String() {
        mockClient.setResponseBody(new GetRecordAclResponseBody(createTestRecordRights(), 2));

        assertThat(sut.getRecordAcl(1, "en")).containsExactly(createTestRecordRight());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORD_ACL);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetRecordAclRequest().setApp(1L).setLang("en"));
    }

    @Test
    public void getRecordAcl_GetRecordAclRequest() {
        GetRecordAclRequest req = new GetRecordAclRequest();
        GetRecordAclResponseBody resp = new GetRecordAclResponseBody(Collections.emptyList(), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getRecordAcl(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORD_ACL);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getRecordAclPreview_long() {
        mockClient.setResponseBody(new GetRecordAclPreviewResponseBody(createTestRecordRights(), 2));

        assertThat(sut.getRecordAclPreview(1)).containsExactly(createTestRecordRight());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORD_ACL_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetRecordAclPreviewRequest().setApp(1L).setLang(null));
    }

    @Test
    public void getRecordAclPreview_long_String() {
        mockClient.setResponseBody(new GetRecordAclPreviewResponseBody(createTestRecordRights(), 2));

        assertThat(sut.getRecordAclPreview(1, "en")).containsExactly(createTestRecordRight());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORD_ACL_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetRecordAclPreviewRequest().setApp(1L).setLang("en"));
    }

    @Test
    public void getRecordAclPreview_GetRecordAclPreviewRequest() {
        GetRecordAclPreviewRequest req = new GetRecordAclPreviewRequest();
        GetRecordAclPreviewResponseBody resp =
                new GetRecordAclPreviewResponseBody(Collections.emptyList(), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getRecordAclPreview(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_RECORD_ACL_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getReminderNotifications_long() {
        GetReminderNotificationsResponseBody resp =
                new GetReminderNotificationsResponseBody(null, "UTC", 1L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getReminderNotifications(10L)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_REMINDER_NOTIFICATIONS);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetReminderNotificationsRequest().setApp(10L));
    }

    @Test
    public void getReminderNotifications_long_String() {
        GetReminderNotificationsResponseBody resp =
                new GetReminderNotificationsResponseBody(null, "UTC", 1L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getReminderNotifications(10L, "en")).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_REMINDER_NOTIFICATIONS);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetReminderNotificationsRequest().setApp(10L).setLang("en"));
    }

    @Test
    public void getReminderNotifications_GetReminderNotificationsRequest() {
        GetReminderNotificationsRequest req = new GetReminderNotificationsRequest().setApp(10L);
        GetReminderNotificationsResponseBody resp =
                new GetReminderNotificationsResponseBody(null, "UTC", 1L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getReminderNotifications(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_REMINDER_NOTIFICATIONS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getReminderNotificationsPreview_long() {
        GetReminderNotificationsPreviewResponseBody resp =
                new GetReminderNotificationsPreviewResponseBody(null, "UTC", 1L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getReminderNotificationsPreview(10L)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_REMINDER_NOTIFICATIONS_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetReminderNotificationsPreviewRequest().setApp(10L));
    }

    @Test
    public void getReminderNotificationsPreview_long_String() {
        GetReminderNotificationsPreviewResponseBody resp =
                new GetReminderNotificationsPreviewResponseBody(null, "UTC", 1L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getReminderNotificationsPreview(10L, "en")).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_REMINDER_NOTIFICATIONS_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetReminderNotificationsPreviewRequest().setApp(10L).setLang("en"));
    }

    @Test
    public void getReminderNotificationsPreview_GetReminderNotificationsPreviewRequest() {
        GetReminderNotificationsPreviewRequest req =
                new GetReminderNotificationsPreviewRequest().setApp(10L);
        GetReminderNotificationsPreviewResponseBody resp =
                new GetReminderNotificationsPreviewResponseBody(null, "UTC", 1L);
        mockClient.setResponseBody(resp);

        assertThat(sut.getReminderNotificationsPreview(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_REMINDER_NOTIFICATIONS_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getReports_long() {
        mockClient.setResponseBody(new GetReportsResponseBody(createTestReports(), 2));

        Map<String, Report> result = sut.getReports(1);
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("report1", createTestReport());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_REPORTS);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetReportsRequest().setApp(1L).setLang(null));
    }

    @Test
    public void getReports_long_String() {
        mockClient.setResponseBody(new GetReportsResponseBody(createTestReports(), 2));

        Map<String, Report> result = sut.getReports(1, "en");
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("report1", createTestReport());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_REPORTS);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetReportsRequest().setApp(1L).setLang("en"));
    }

    @Test
    public void getReports_GetReportsRequest() {
        GetReportsRequest req = new GetReportsRequest();
        GetReportsResponseBody resp = new GetReportsResponseBody(Collections.emptyMap(), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getReports(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_REPORTS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getReportsPreview_long() {
        mockClient.setResponseBody(new GetReportsPreviewResponseBody(createTestReports(), 2));

        Map<String, Report> result = sut.getReportsPreview(1);
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("report1", createTestReport());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_REPORTS_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetReportsPreviewRequest().setApp(1L).setLang(null));
    }

    @Test
    public void getReportsPreview_long_String() {
        mockClient.setResponseBody(new GetReportsPreviewResponseBody(createTestReports(), 2));

        Map<String, Report> result = sut.getReportsPreview(1, "en");
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("report1", createTestReport());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_REPORTS_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetReportsPreviewRequest().setApp(1L).setLang("en"));
    }

    @Test
    public void getReportsPreview_GetReportsPreviewRequest() {
        GetReportsPreviewRequest req = new GetReportsPreviewRequest();
        GetReportsPreviewResponseBody resp =
                new GetReportsPreviewResponseBody(Collections.emptyMap(), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getReportsPreview(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_REPORTS_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getViews_long() {
        mockClient.setResponseBody(new GetViewsResponseBody(createTestViews(), 2));

        Map<String, View> result = sut.getViews(1);
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("view1", createTestView());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_VIEWS);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetViewsRequest().setApp(1L).setLang(null));
    }

    @Test
    public void getViews_long_String() {
        mockClient.setResponseBody(new GetViewsResponseBody(createTestViews(), 2));

        Map<String, View> result = sut.getViews(1, "en");
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("view1", createTestView());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_VIEWS);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetViewsRequest().setApp(1L).setLang("en"));
    }

    @Test
    public void getViews_GetViewsRequest() {
        GetViewsRequest req = new GetViewsRequest();
        GetViewsResponseBody resp = new GetViewsResponseBody(Collections.emptyMap(), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getViews(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_VIEWS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getViewsPreview_long() {
        mockClient.setResponseBody(new GetViewsPreviewResponseBody(createTestViews(), 2));

        Map<String, View> result = sut.getViewsPreview(1);
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("view1", createTestView());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_VIEWS_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetViewsPreviewRequest().setApp(1L).setLang(null));
    }

    @Test
    public void getViewsPreview_long_String() {
        mockClient.setResponseBody(new GetViewsPreviewResponseBody(createTestViews(), 2));

        Map<String, View> result = sut.getViewsPreview(1, "en");
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("view1", createTestView());
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_VIEWS_PREVIEW);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetViewsPreviewRequest().setApp(1L).setLang("en"));
    }

    @Test
    public void getViewsPreview_GetViewsPreviewRequest() {
        GetViewsPreviewRequest req = new GetViewsPreviewRequest();
        GetViewsPreviewResponseBody resp = new GetViewsPreviewResponseBody(Collections.emptyMap(), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.getViewsPreview(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_VIEWS_PREVIEW);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void moveToSpace_long() {
        mockClient.setResponseBody(new MoveAppResponseBody());

        sut.move(1);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.MOVE_APP_TO_SPACE);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(new MoveAppRequest().setApp(1L).setSpace(null));
    }

    @Test
    public void moveToSpace_long_long() {
        mockClient.setResponseBody(new MoveAppResponseBody());

        sut.move(1, 2L);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.MOVE_APP_TO_SPACE);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(new MoveAppRequest().setApp(1L).setSpace(2L));
    }

    @Test
    public void moveToSpace_long_null() {
        mockClient.setResponseBody(new MoveAppResponseBody());

        sut.move(1, null);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.MOVE_APP_TO_SPACE);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(new MoveAppRequest().setApp(1L).setSpace(null));
    }

    @Test
    public void moveToSpace_MoveToSpaceRequest() {
        MoveAppRequest req = new MoveAppRequest();
        MoveAppResponseBody resp = new MoveAppResponseBody();
        mockClient.setResponseBody(resp);

        assertThat(sut.move(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.MOVE_APP_TO_SPACE);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void revertApp_long() {
        mockClient.setResponseBody(new DeployAppResponseBody());

        sut.revertApp(1);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DEPLOY_APP);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new DeployAppRequest()
                                .setApps(Collections.singletonList(new DeployApp().setApp(1L).setRevision(null)))
                                .setRevert(true));
    }

    @Test
    public void revertApp_long_Long() {
        mockClient.setResponseBody(new DeployAppResponseBody());

        sut.revertApp(1, 2L);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.DEPLOY_APP);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new DeployAppRequest()
                                .setApps(Collections.singletonList(new DeployApp().setApp(1L).setRevision(2L)))
                                .setRevert(true));
    }

    @Test
    public void updateAdminNotes_UpdateAdminNotesRequest() {
        UpdateAdminNotesRequest req = new UpdateAdminNotesRequest();
        UpdateAdminNotesResponseBody resp = new UpdateAdminNotesResponseBody(1);
        mockClient.setResponseBody(resp);

        assertThat(sut.updateAdminNotes(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_APP_ADMIN_NOTES);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateAppAcl_long_List() {
        mockClient.setResponseBody(new UpdateAppAclResponseBody(2));

        assertThat(sut.updateAppAcl(1, createTestAppRightEntities())).isEqualTo(2);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_APP_ACL);
        assertThat(mockClient.getLastBody())
                .isEqualTo(
                        new UpdateAppAclRequest()
                                .setApp(1L)
                                .setRights(createTestAppRightEntities())
                                .setRevision(null));
    }

    @Test
    public void updateAppAcl_long_List_Long() {
        mockClient.setResponseBody(new UpdateAppAclResponseBody(3));

        assertThat(sut.updateAppAcl(1, createTestAppRightEntities(), 2L)).isEqualTo(3);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_APP_ACL);
        assertThat(mockClient.getLastBody())
                .isEqualTo(
                        new UpdateAppAclRequest()
                                .setApp(1L)
                                .setRights(createTestAppRightEntities())
                                .setRevision(2L));
    }

    @Test
    public void updateAppAcl_UpdateAppAclRequest() {
        UpdateAppAclRequest req = new UpdateAppAclRequest();
        UpdateAppAclResponseBody resp = new UpdateAppAclResponseBody(1);
        mockClient.setResponseBody(resp);

        assertThat(sut.updateAppAcl(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_APP_ACL);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateAppActions_long_Map() {
        Map<String, AppAction> actions = Collections.singletonMap("action1", new AppAction());
        Map<String, ActionId> ids = Collections.singletonMap("action1", new ActionId(100));
        mockClient.setResponseBody(new UpdateAppActionsResponseBody(ids, 1L));

        assertThat(sut.updateAppActions(1L, actions)).isEqualTo(ids);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_APP_ACTIONS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(new UpdateAppActionsRequest().setApp(1L).setActions(actions));
    }

    @Test
    public void updateAppActions_long_Map_Long() {
        Map<String, AppAction> actions = Collections.singletonMap("action1", new AppAction());
        Map<String, ActionId> ids = Collections.singletonMap("action1", new ActionId(100));
        mockClient.setResponseBody(new UpdateAppActionsResponseBody(ids, 1L));

        assertThat(sut.updateAppActions(1L, actions, 2L)).isEqualTo(ids);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_APP_ACTIONS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(new UpdateAppActionsRequest().setApp(1L).setActions(actions).setRevision(2L));
    }

    @Test
    public void updateAppActions_UpdateAppActionsRequest() {
        UpdateAppActionsRequest req = new UpdateAppActionsRequest();
        UpdateAppActionsResponseBody resp =
                new UpdateAppActionsResponseBody(Collections.emptyMap(), 1L);
        mockClient.setResponseBody(resp);

        assertThat(sut.updateAppActions(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_APP_ACTIONS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateAppCustomize_UpdateAppCustomizeRequest() {
        UpdateAppCustomizeRequest req = new UpdateAppCustomizeRequest();
        UpdateAppCustomizeResponseBody resp = new UpdateAppCustomizeResponseBody(1);
        mockClient.setResponseBody(resp);

        assertThat(sut.updateAppCustomize(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_APP_CUSTOMIZE);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateAppSettings_UpdateAppSettingsRequest() {
        UpdateAppSettingsRequest req = new UpdateAppSettingsRequest();
        UpdateAppSettingsResponseBody resp = new UpdateAppSettingsResponseBody(1);
        mockClient.setResponseBody(resp);

        assertThat(sut.updateAppSettings(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_APP_SETTINGS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateFieldAcl_long_List() {
        mockClient.setResponseBody(new UpdateFieldAclResponseBody(2));

        assertThat(sut.updateFieldAcl(1, createTestFieldRights())).isEqualTo(2);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_FIELD_ACL);
        assertThat(mockClient.getLastBody())
                .isEqualTo(
                        new UpdateFieldAclRequest()
                                .setApp(1L)
                                .setRights(createTestFieldRights())
                                .setRevision(null));
    }

    @Test
    public void updateFieldAcl_long_List_Long() {
        mockClient.setResponseBody(new UpdateFieldAclResponseBody(18));

        assertThat(sut.updateFieldAcl(1, createTestFieldRights(), 3L)).isEqualTo(18);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_FIELD_ACL);
        assertThat(mockClient.getLastBody())
                .isEqualTo(
                        new UpdateFieldAclRequest()
                                .setApp(1L)
                                .setRights(createTestFieldRights())
                                .setRevision(3L));
    }

    @Test
    public void updateFieldAcl_UpdateFieldAclRequest() {
        UpdateFieldAclRequest req = new UpdateFieldAclRequest();
        UpdateFieldAclResponseBody resp = new UpdateFieldAclResponseBody(1);
        mockClient.setResponseBody(resp);

        assertThat(sut.updateFieldAcl(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_FIELD_ACL);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateFormFields_long_List() {
        mockClient.setResponseBody(new UpdateFormFieldsResponseBody(2));

        List<FieldProperty> props = Collections.singletonList(createTestField());
        assertThat(sut.updateFormFields(1, props)).isEqualTo(2);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_FORM_FIELDS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new UpdateFormFieldsRequest()
                                .setApp(1L)
                                .setProperties(createTestProperty())
                                .setRevision(null));
    }

    @Test
    public void updateFormFields_long_List_Long() {
        mockClient.setResponseBody(new UpdateFormFieldsResponseBody(3));

        List<FieldProperty> props = Collections.singletonList(createTestField());
        assertThat(sut.updateFormFields(1, props, 2L)).isEqualTo(3);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_FORM_FIELDS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new UpdateFormFieldsRequest()
                                .setApp(1L)
                                .setProperties(createTestProperty())
                                .setRevision(2L));
    }

    @Test
    public void updateFormFields_long_Map() {
        mockClient.setResponseBody(new UpdateFormFieldsResponseBody(2));

        assertThat(sut.updateFormFields(1, createTestProperty())).isEqualTo(2);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_FORM_FIELDS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new UpdateFormFieldsRequest()
                                .setApp(1L)
                                .setProperties(createTestProperty())
                                .setRevision(null));
    }

    @Test
    public void updateFormFields_long_Map_Long() {
        mockClient.setResponseBody(new UpdateFormFieldsResponseBody(3));

        assertThat(sut.updateFormFields(1, createTestProperty(), 2L)).isEqualTo(3);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_FORM_FIELDS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new UpdateFormFieldsRequest()
                                .setApp(1L)
                                .setProperties(createTestProperty())
                                .setRevision(2L));
    }

    @Test
    public void updateFormFields_UpdateFormFieldsRequest() {
        UpdateFormFieldsRequest req = new UpdateFormFieldsRequest();
        UpdateFormFieldsResponseBody resp = new UpdateFormFieldsResponseBody(1);
        mockClient.setResponseBody(resp);

        assertThat(sut.updateFormFields(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_FORM_FIELDS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateFormLayout_long_List() {
        mockClient.setResponseBody(new UpdateFormLayoutResponseBody(2));

        assertThat(sut.updateFormLayout(1, createTestLayout())).isEqualTo(2);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_FORM_LAYOUT);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new UpdateFormLayoutRequest()
                                .setApp(1L)
                                .setLayout(createTestLayout())
                                .setRevision(null));
    }

    @Test
    public void updateFormLayout_long_List_Long() {
        mockClient.setResponseBody(new UpdateFormLayoutResponseBody(3));

        assertThat(sut.updateFormLayout(1, createTestLayout(), 2L)).isEqualTo(3);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_FORM_LAYOUT);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new UpdateFormLayoutRequest().setApp(1L).setLayout(createTestLayout()).setRevision(2L));
    }

    @Test
    public void updateFormLayout_UpdateLayoutRequest() {
        UpdateFormLayoutRequest req = new UpdateFormLayoutRequest();
        UpdateFormLayoutResponseBody resp = new UpdateFormLayoutResponseBody(1);
        mockClient.setResponseBody(resp);

        assertThat(sut.updateFormLayout(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_FORM_LAYOUT);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateGeneralNotifications_UpdateGeneralNotificationsRequest() {
        UpdateGeneralNotificationsRequest req = new UpdateGeneralNotificationsRequest();
        UpdateGeneralNotificationsResponseBody resp = new UpdateGeneralNotificationsResponseBody(1);
        mockClient.setResponseBody(resp);

        assertThat(sut.updateGeneralNotifications(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_GENERAL_NOTIFICATIONS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updatePerRecordNotifications_UpdatePerRecordNotificationsRequest() {
        UpdatePerRecordNotificationsRequest req = new UpdatePerRecordNotificationsRequest();
        UpdatePerRecordNotificationsResponseBody resp = new UpdatePerRecordNotificationsResponseBody(1);
        mockClient.setResponseBody(resp);

        assertThat(sut.updatePerRecordNotifications(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_PRE_RECORD_NOTIFICATIONS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateProcessManagement_UpdateProcessManagementRequest() {
        UpdateProcessManagementRequest req = new UpdateProcessManagementRequest();
        UpdateProcessManagementResponseBody resp = new UpdateProcessManagementResponseBody(1);
        mockClient.setResponseBody(resp);

        assertThat(sut.updateProcessManagement(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_PROCESS_MANAGEMENT);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateRecordAcl_long_List() {
        mockClient.setResponseBody(new UpdateRecordAclResponseBody(2));

        assertThat(sut.updateRecordAcl(1, createTestRecordRights())).isEqualTo(2);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_RECORD_ACL);
        assertThat(mockClient.getLastBody())
                .isEqualTo(
                        new UpdateRecordAclRequest()
                                .setApp(1L)
                                .setRights(createTestRecordRights())
                                .setRevision(null));
    }

    @Test
    public void updateRecordAcl_long_List_Long() {
        mockClient.setResponseBody(new UpdateRecordAclResponseBody(3));

        assertThat(sut.updateRecordAcl(1, createTestRecordRights(), 2L)).isEqualTo(3);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_RECORD_ACL);
        assertThat(mockClient.getLastBody())
                .isEqualTo(
                        new UpdateRecordAclRequest()
                                .setApp(1L)
                                .setRights(createTestRecordRights())
                                .setRevision(2L));
    }

    @Test
    public void updateRecordAcl_UpdateRecordAclRequest() {
        UpdateRecordAclRequest req = new UpdateRecordAclRequest();
        UpdateRecordAclResponseBody resp = new UpdateRecordAclResponseBody(1);
        mockClient.setResponseBody(resp);

        assertThat(sut.updateRecordAcl(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_RECORD_ACL);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateReminderNotifications_UpdateReminderNotificationsRequest() {
        UpdateReminderNotificationsRequest req = new UpdateReminderNotificationsRequest();
        UpdateReminderNotificationsResponseBody resp = new UpdateReminderNotificationsResponseBody(1);
        mockClient.setResponseBody(resp);

        assertThat(sut.updateReminderNotifications(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_REMINDER_NOTIFICATIONS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateReports_long_Map() {
        Map<String, ReportId> ids = Collections.singletonMap("report1", new ReportId(100));
        mockClient.setResponseBody(new UpdateReportsResponseBody(ids, 2L));

        Map<String, ReportId> result = sut.updateReports(1, createTestReports());
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("report1", new ReportId(100));
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_REPORTS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(new UpdateReportsRequest().setApp(1L).setReports(createTestReports()));
    }

    @Test
    public void updateReports_long_Map_Long() {
        Map<String, ReportId> ids = Collections.singletonMap("report2", new ReportId(200));
        mockClient.setResponseBody(new UpdateReportsResponseBody(ids, 3L));

        Map<String, ReportId> result = sut.updateReports(1, createTestReports(), 3L);
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("report2", new ReportId(200));
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_REPORTS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(
                        new UpdateReportsRequest().setApp(1L).setReports(createTestReports()).setRevision(3L));
    }

    @Test
    public void updateReports_UpdateReportsRequest() {
        UpdateReportsRequest req = new UpdateReportsRequest();
        UpdateReportsResponseBody resp = new UpdateReportsResponseBody(Collections.emptyMap(), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.updateReports(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_REPORTS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updateViews_long_Map() {
        Map<String, ViewId> ids = Collections.singletonMap("view1", new ViewId(100));
        mockClient.setResponseBody(new UpdateViewsResponseBody(ids, 2L));

        Map<String, ViewId> result = sut.updateViews(1, createTestViews());
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("view1", new ViewId(100));
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_VIEWS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(new UpdateViewsRequest().setApp(1L).setViews(createTestViews()));
    }

    @Test
    public void updateViews_long_Map_Long() {
        Map<String, ViewId> ids = Collections.singletonMap("view2", new ViewId(42));
        mockClient.setResponseBody(new UpdateViewsResponseBody(ids, 3L));

        Map<String, ViewId> result = sut.updateViews(1, createTestViews(), 3L);
        assertThat(result).hasSize(1);
        assertThat(result).containsEntry("view2", new ViewId(42));
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_VIEWS);
        assertThat(mockClient.getLastBody())
                .usingRecursiveComparison()
                .isEqualTo(new UpdateViewsRequest().setApp(1L).setViews(createTestViews()).setRevision(3L));
    }

    @Test
    public void updateViews_UpdateViewsRequest() {
        UpdateViewsRequest req = new UpdateViewsRequest();
        UpdateViewsResponseBody resp = new UpdateViewsResponseBody(Collections.emptyMap(), 1);
        mockClient.setResponseBody(resp);

        assertThat(sut.updateViews(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_VIEWS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }
}
