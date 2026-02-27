package com.kintone.client.helper;

import com.kintone.client.KintoneClient;
import com.kintone.client.api.app.*;
import com.kintone.client.model.FileBody;
import com.kintone.client.model.Group;
import com.kintone.client.model.Organization;
import com.kintone.client.model.User;
import com.kintone.client.model.app.*;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.app.layout.Layout;
import com.kintone.client.model.app.report.Report;
import com.kintone.client.model.record.*;
import com.kintone.client.model.record.Record;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Value;

public class App {
    private static final int MAX_DEPLOY_WAIT_SEC = 180;

    private final KintoneClient client;
    private final long appId;

    // 組み込みフィールド
    private final Map<FieldType, FieldProperty> builtinFields = new TreeMap<>();
    // 組み込み以外・サブテーブル内フィールド以外の、フィールドコードがあるフィールド
    private final Map<String, FieldProperty> customFields = new TreeMap<>();

    public static App create(KintoneClient client, String name) {
        long appId = client.app().addApp(name);
        return new App(client, appId);
    }

    public static App create(KintoneClient client, String name, long spaceId, long threadId) {
        long appId = client.app().addApp(name, spaceId, threadId);
        return new App(client, appId);
    }

    public static App fromExisting(KintoneClient client, long appId) {
        return new App(client, appId);
    }

    private App(KintoneClient client, long appId) {
        this.client = client;
        this.appId = appId;
    }

    /* キャッシュされたアプリ情報の取得 */
    public long id() {
        return appId;
    }

    public FieldProperty field(String code) {
        FieldProperty field = findFieldByCode(code);
        if (field != null) {
            return field;
        }
        refreshFieldCache();
        return findFieldByCode(code);
    }

    private FieldProperty findFieldByCode(String code) {
        FieldProperty field = customFields.get(code);
        if (field != null) {
            return field;
        }
        return builtinFields.values().stream()
                .filter(f -> code.equals(f.getCode()))
                .findFirst()
                .orElse(null);
    }

    public FieldProperty field(FieldType type) {
        FieldProperty field = findFirstFieldByType(type);
        if (field != null) {
            return field;
        }
        refreshFieldCache();
        return findFirstFieldByType(type);
    }

    private FieldProperty findFirstFieldByType(FieldType type) {
        FieldProperty field = builtinFields.get(type);
        if (field != null) {
            return field;
        }
        return customFields.values().stream().filter(f -> f.getType() == type).findFirst().orElse(null);
    }

    // フィールド情報のキャッシュをクリアして取得し直す
    private void refreshFieldCache() {
        builtinFields.clear();
        customFields.clear();

        Map<String, FieldProperty> fields = getFields(true);
        updateFieldCache(fields.values());
    }

    // フィールド情報のキャッシュを追加・更新する。クリアはしない
    private void updateFieldCache(Collection<FieldProperty> fields) {
        for (FieldProperty field : fields) {
            if (field.getType().isBuiltin()) {
                builtinFields.put(field.getType(), field);
            } else {
                customFields.put(field.getCode(), field);
            }
        }
    }

    private void removeFieldCache(List<String> codes) {
        for (String code : codes) {
            customFields.remove(code);
        }
    }

    /* deploy */
    public App deploy() {
        client.app().deployApp(appId);
        waitDeploy();
        return this;
    }

    public void waitDeploy() {
        for (int i = 0; i < MAX_DEPLOY_WAIT_SEC; i++) {
            DeployStatus status = client.app().getDeployStatus(appId);
            if (status == DeployStatus.SUCCESS) {
                return;
            } else if (status != DeployStatus.PROCESSING) {
                String msg = String.format("deploy failed or canceled. app:%d, status:%s", appId, status);
                throw new RuntimeException(msg);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* general settings */
    public long getAppRevision(boolean preview) {
        return getAppSettings(preview).getRevision();
    }

    public AppSettings getAppSettings(boolean preview) {
        if (preview) {
            GetAppSettingsPreviewResponseBody resp = client.app().getAppSettingsPreview(appId);
            return new AppSettings(
                    resp.getName(),
                    resp.getDescription(),
                    resp.getIcon(),
                    resp.getTheme(),
                    resp.getRevision());
        } else {
            GetAppSettingsResponseBody resp = client.app().getAppSettings(appId);
            return new AppSettings(
                    resp.getName(),
                    resp.getDescription(),
                    resp.getIcon(),
                    resp.getTheme(),
                    resp.getRevision());
        }
    }

    public App updateAppSettings(AppSettingsBuilder builder) {
        client.app().updateAppSettings(builder.build(appId));
        return this;
    }

    @Value
    public static class AppSettings {
        private final String name;
        private final String description;
        private final AppIcon icon;
        private final String theme;
        private final long revision;
    }

    public Map<String, AppAction> getActions(boolean preview) {
        if (preview) {
            return client.app().getAppActionsPreview(appId);
        } else {
            return client.app().getAppActions(appId);
        }
    }

    public App updateActions(AppAction... actions) {
        Map<String, AppAction> actionMap =
                Stream.of(actions).collect(Collectors.toMap(AppAction::getName, Function.identity()));
        return updateActions(actionMap);
    }

    public App updateActions(Map<String, AppAction> actions) {
        client.app().updateAppActions(appId, actions);
        return this;
    }

    public AppCustomize getAppCustomize(boolean preview) {
        if (preview) {
            GetAppCustomizePreviewResponseBody resp = client.app().getAppCustomizePreview(appId);
            return new AppCustomize(
                    resp.getScope(), resp.getDesktop(), resp.getMobile(), resp.getRevision());
        } else {
            GetAppCustomizeResponseBody resp = client.app().getAppCustomize(appId);
            return new AppCustomize(
                    resp.getScope(), resp.getDesktop(), resp.getMobile(), resp.getRevision());
        }
    }

    public App updateAppCustomize(AppCustomizeBuilder builder) {
        client.app().updateAppCustomize(builder.build(appId));
        return this;
    }

    @Value
    public static class AppCustomize {
        private final CustomizeScope scope;
        private final CustomizeBody desktop;
        private final CustomizeBody mobile;
        private final long revision;
    }

    /* fields */
    public App addFields(FieldProperty... fields) {
        return addFields(Arrays.asList(fields));
    }

    public App addFields(List<FieldProperty> fields) {
        client.app().addFormFields(appId, fields);
        updateFieldCache(fields);
        return this;
    }

    public App deleteFields(String... codes) {
        List<String> list = Arrays.asList(codes);
        client.app().deleteFormFields(appId, list);
        removeFieldCache(list);
        return this;
    }

    public Map<String, FieldProperty> getFields(boolean preview) {
        if (preview) {
            return client.app().getFormFieldsPreview(appId);
        } else {
            return client.app().getFormFields(appId);
        }
    }

    public List<Layout> getLayout(boolean preview) {
        if (preview) {
            return client.app().getFormLayoutPreview(appId);
        } else {
            return client.app().getFormLayout(appId);
        }
    }

    public App updateLayout(FormLayoutBuilder builder) {
        client.app().updateFormLayout(appId, builder.build());
        return this;
    }

    /* acl */
    public List<AppRightEntity> getAppAcl(boolean preview) {
        if (preview) {
            return client.app().getAppAclPreview(appId);
        } else {
            return client.app().getAppAcl(appId);
        }
    }

    public App updateAppAcl(AppAclBuilder builder) {
        client.app().updateAppAcl(builder.build(appId));
        return this;
    }

    public List<RecordRight> getRecordAcl(boolean preview) {
        if (preview) {
            return client.app().getRecordAclPreview(appId);
        } else {
            return client.app().getRecordAcl(appId);
        }
    }

    public App updateRecordAcl(RecordAclBuilder builder) {
        client.app().updateRecordAcl(builder.build(appId));
        return this;
    }

    public List<FieldRight> getFieldAcl(boolean preview) {
        if (preview) {
            return client.app().getFieldAclPreview(appId);
        } else {
            return client.app().getFieldAcl(appId);
        }
    }

    public App updateFieldAcl(FieldAclBuilder builder) {
        client.app().updateFieldAcl(builder.build(appId));
        return this;
    }

    /* notification settings */
    public GeneralNotifications getGeneralNotifications(boolean preview) {
        if (preview) {
            GetGeneralNotificationsPreviewResponseBody resp =
                    client.app().getGeneralNotificationsPreview(appId);
            return new GeneralNotifications(resp.getNotifications(), resp.isNotifyToCommenter());
        } else {
            GetGeneralNotificationsResponseBody resp = client.app().getGeneralNotifications(appId);
            return new GeneralNotifications(resp.getNotifications(), resp.isNotifyToCommenter());
        }
    }

    @Value
    public static class GeneralNotifications {
        private final List<GeneralNotification> notifications;
        private final boolean notifyToCommenter;
    }

    public App updateGeneralNotifications(GeneralNotificationsBuilder builder) {
        client.app().updateGeneralNotifications(builder.build(appId));
        return this;
    }

    public List<PerRecordNotification> getRecordNotifications(boolean preview) {
        if (preview) {
            return client.app().getPerRecordNotificationsPreview(appId).getNotifications();
        } else {
            return client.app().getPerRecordNotifications(appId).getNotifications();
        }
    }

    public App updateRecordNotifications(RecordNotificationsBuilder builder) {
        client.app().updatePerRecordNotifications(builder.build(appId));
        return this;
    }

    public ReminderNotifications getReminderNotifications(boolean preview) {
        if (preview) {
            GetReminderNotificationsPreviewResponseBody resp =
                    client.app().getReminderNotificationsPreview(appId);
            return new ReminderNotifications(resp.getNotifications(), resp.getTimezone());
        } else {
            GetReminderNotificationsResponseBody resp = client.app().getReminderNotifications(appId);
            return new ReminderNotifications(resp.getNotifications(), resp.getTimezone());
        }
    }

    @Value
    public static class ReminderNotifications {
        private final List<ReminderNotification> notifications;
        private final String timezone;
    }

    public App updateReminderNotifications(ReminderNotificationsBuilder builder) {
        client.app().updateReminderNotifications(builder.build(appId));
        return this;
    }

    /* process management settings */
    public ProcessManagement getProcessManagement(boolean preview) {
        if (preview) {
            GetProcessManagementPreviewResponseBody resp =
                    client.app().getProcessManagementPreview(appId);
            return new ProcessManagement(
                    resp.isEnable(), resp.getStates(), resp.getActions(), resp.getRevision());
        } else {
            GetProcessManagementResponseBody resp = client.app().getProcessManagement(appId);
            return new ProcessManagement(
                    resp.isEnable(), resp.getStates(), resp.getActions(), resp.getRevision());
        }
    }

    @Value
    public static class ProcessManagement {
        private final boolean enable;
        private final Map<String, ProcessState> states;
        private final List<ProcessAction> actions;
        private final long revision;
    }

    public App updateProcessManagement(ProcessManagementBuilder builder) {
        client.app().updateProcessManagement(builder.build(appId));
        return this;
    }

    public App applyExampleProcessManagement() {
        return updateProcessManagement(ProcessManagementBuilder.example());
    }

    /* reports */
    public Map<String, Report> getReports(boolean preview) {
        if (preview) {
            return client.app().getReportsPreview(appId);
        } else {
            return client.app().getReports(appId);
        }
    }

    public App updateReports(Report... reports) {
        Map<String, Report> reportMap = new HashMap<>();
        for (Report report : reports) {
            reportMap.put(report.getName(), report);
        }
        return updateReports(reportMap);
    }

    public App updateReports(Map<String, Report> reports) {
        client.app().updateReports(appId, reports);
        return this;
    }

    /* views */
    public Map<String, View> getViews(boolean preview) {
        if (preview) {
            return client.app().getViewsPreview(appId);
        } else {
            return client.app().getViews(appId);
        }
    }

    public App updateViews(View... views) {
        Map<String, View> viewMap = new HashMap<>();
        for (View view : views) {
            viewMap.put(view.getName(), view);
        }
        return updateViews(viewMap);
    }

    public App updateViews(Map<String, View> views) {
        client.app().updateViews(appId, views);
        return this;
    }

    /* records */
    public Record getRecord(long recordId) {
        return client.record().getRecord(appId, recordId);
    }

    public List<Record> getRecords() {
        return client.record().getRecords(appId);
    }

    /**
     * レコードを追加する
     *
     * @param fieldAndValues 奇数番目はFieldProperty, 偶数番目はその値の文字列
     * @return 追加したレコード番号
     */
    public long addRecord(Object... fieldAndValues) {
        if (fieldAndValues.length % 2 != 0) {
            throw new RuntimeException("invalid argument length.");
        }

        Record record = new Record();
        for (int i = 0; i < fieldAndValues.length / 2; i++) {
            FieldProperty schema = (FieldProperty) fieldAndValues[i * 2];
            String value = (String) fieldAndValues[i * 2 + 1];
            record.putField(schema.getCode(), makeValue(schema.getType(), value));
        }
        return client.record().addRecord(appId, record);
    }

    private FieldValue makeValue(FieldType type, String value) {
        switch (type) {
            case CHECK_BOX:
                if (value == null || value.isEmpty()) {
                    return new CheckBoxFieldValue();
                }
                return new CheckBoxFieldValue(value.split(","));
            case CREATED_TIME:
                return new CreatedTimeFieldValue(ZonedDateTime.parse(value));
            case CREATOR:
                return new CreatorFieldValue(new User(value));
            case DATE:
                return new DateFieldValue(LocalDate.parse(value));
            case DATETIME:
                return new DateTimeFieldValue(ZonedDateTime.parse(value));
            case DROP_DOWN:
                return new DropDownFieldValue(value);
            case FILE:
                if (value == null || value.isEmpty()) {
                    return new FileFieldValue();
                }
                List<FileBody> files =
                        Arrays.stream(value.split(","))
                                .map(v -> new FileBody().setFileKey(v))
                                .collect(Collectors.toList());
                return new FileFieldValue(files);
            case GROUP_SELECT:
                if (value == null || value.isEmpty()) {
                    return new GroupSelectFieldValue();
                }
                List<Group> groups =
                        Arrays.stream(value.split(",")).map(Group::new).collect(Collectors.toList());
                return new GroupSelectFieldValue(groups);
            case LINK:
                return new LinkFieldValue(value);
            case MODIFIER:
                return new ModifierFieldValue(new User(value));
            case MULTI_LINE_TEXT:
                return new MultiLineTextFieldValue(value);
            case MULTI_SELECT:
                if (value == null || value.isEmpty()) {
                    return new MultiSelectFieldValue();
                }
                return new MultiSelectFieldValue(value.split(","));
            case NUMBER:
                return new NumberFieldValue(new BigDecimal(value));
            case ORGANIZATION_SELECT:
                if (value == null || value.isEmpty()) {
                    return new OrganizationSelectFieldValue();
                }
                List<Organization> orgs =
                        Arrays.stream(value.split(",")).map(Organization::new).collect(Collectors.toList());
                return new OrganizationSelectFieldValue(orgs);
            case RADIO_BUTTON:
                return new RadioButtonFieldValue(value);
            case RICH_TEXT:
                return new RichTextFieldValue(value);
            case SINGLE_LINE_TEXT:
                return new SingleLineTextFieldValue(value);
            case TIME:
                return new TimeFieldValue(LocalTime.parse(value));
            case UPDATED_TIME:
                return new UpdatedTimeFieldValue(ZonedDateTime.parse(value));
            case USER_SELECT:
                if (value == null || value.isEmpty()) {
                    return new UserSelectFieldValue();
                }
                List<User> users =
                        Arrays.stream(value.split(",")).map(User::new).collect(Collectors.toList());
                return new UserSelectFieldValue(users);
                /* 以下はサポートしない */
            case CALC:
            case CATEGORY:
            case GROUP:
            case HR:
            case LABEL:
            case RECORD_NUMBER:
            case REFERENCE_TABLE:
            case SPACER:
            case STATUS:
            case STATUS_ASSIGNEE:
            case SUBTABLE:
            default:
                throw new AssertionError("unsupported type: " + type);
        }
    }

    public List<Long> addRecords(List<Record> records) {
        return client.record().addRecords(appId, records);
    }
}
