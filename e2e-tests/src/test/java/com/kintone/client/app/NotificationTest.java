package com.kintone.client.app;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.*;
import com.kintone.client.api.app.*;
import com.kintone.client.helper.*;
import com.kintone.client.helper.App;
import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import com.kintone.client.model.app.*;
import com.kintone.client.model.app.field.FieldProperty;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/** AppClientのnotifications/*.jsonのテスト */
public class NotificationTest extends ApiTestBase {
    @Test
    public void getGeneralNotifications_getGeneralNotificationsPreview() {
        KintoneClient client = setupDefaultClient();
        FieldProperty userSelect = Fields.userSelect();
        App app = App.create(client, "getGeneralNotifications").addFields(userSelect);

        GeneralNotificationsBuilder builder = new GeneralNotificationsBuilder();
        builder.notifyToCommenter(true);
        builder.user(getDefaultUser()).all(true);
        builder.everyone().all(false);
        builder.org(Orgs.org1.getCode(), true).statusChanged(true);
        builder.field(userSelect.getCode()).recordAdded(true);
        app.updateGeneralNotifications(builder).deploy();
        long revision = app.getAppRevision(false);

        List<GeneralNotification> notifications = new ArrayList<>();
        Entity user = new Entity(EntityType.USER, getDefaultUser());
        Entity group = new Entity(EntityType.GROUP, "everyone");
        Entity org = new Entity(EntityType.ORGANIZATION, Orgs.org1.getCode());
        Entity field = new Entity(EntityType.FIELD_ENTITY, userSelect.getCode());
        notifications.add(generalNotification(user, true, true, true, true, true));
        notifications.add(generalNotification(group, false, false, false, false, false));
        GeneralNotification n = generalNotification(org, false, false, false, true, false);
        notifications.add(n.setIncludeSubs(true));
        notifications.add(generalNotification(field, true, false, false, false, false));

        GetGeneralNotificationsRequest req1 = new GetGeneralNotificationsRequest();
        req1.setApp(app.id());
        GetGeneralNotificationsResponseBody resp1 = client.app().getGeneralNotifications(req1);
        assertThat(resp1.getRevision()).isEqualTo(revision);
        assertThat(resp1.isNotifyToCommenter()).isTrue();
        assertThat(resp1.getNotifications())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(notifications);

        app.updateGeneralNotifications(new GeneralNotificationsBuilder().notifyToCommenter(false));

        GetGeneralNotificationsPreviewRequest req2 = new GetGeneralNotificationsPreviewRequest();
        req2.setApp(app.id());
        GetGeneralNotificationsPreviewResponseBody resp2 =
                client.app().getGeneralNotificationsPreview(req2);
        assertThat(resp2.getRevision()).isEqualTo(revision + 1);
        assertThat(resp2.isNotifyToCommenter()).isFalse();
        assertThat(resp2.getNotifications())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(notifications);
    }

    @Test
    public void getPerRecordNotifications_getPerRecordNotificationsPreview() {
        KintoneClient client = setupDefaultClient();
        FieldProperty number = Fields.number();
        FieldProperty userSelect = Fields.userSelect();
        App app = App.create(client, "getPerRecordNotifications").addFields(number, userSelect);

        RecordNotificationsBuilder builder = new RecordNotificationsBuilder();
        builder.query(number.getCode() + " >= 1").title("n1").user(getDefaultUser());
        builder.query(number.getCode() + " >= 2").title("n2").everyone().user(getDefaultUser());
        builder
                .query(number.getCode() + " >= 3")
                .title("n3")
                .org(Orgs.org1.getCode(), true)
                .org(Orgs.org2.getCode(), false);
        builder.query(number.getCode() + " >= 4").title("n4").field(userSelect.getCode());
        app.updateRecordNotifications(builder).deploy();
        long revision = app.getAppRevision(false);

        List<PerRecordNotification> notifications = new ArrayList<>();
        List<NotificationTarget> t1 = makeTargets(EntityType.USER, getDefaultUser());
        List<NotificationTarget> t2 =
                makeTargets(EntityType.GROUP, "everyone", EntityType.USER, getDefaultUser());
        List<NotificationTarget> t3 =
                makeTargets(EntityType.ORGANIZATION, Orgs.org1, EntityType.ORGANIZATION, Orgs.org2);
        t3.get(0).setIncludeSubs(true);
        List<NotificationTarget> t4 = makeTargets(EntityType.FIELD_ENTITY, userSelect.getCode());
        notifications.add(perRecordNotification("n1", number.getCode() + " >= 1", t1));
        notifications.add(perRecordNotification("n2", number.getCode() + " >= 2", t2));
        notifications.add(perRecordNotification("n3", number.getCode() + " >= 3", t3));
        notifications.add(perRecordNotification("n4", number.getCode() + " >= 4", t4));

        GetPerRecordNotificationsRequest req1 = new GetPerRecordNotificationsRequest();
        req1.setApp(app.id());
        req1.setLang("default");
        GetPerRecordNotificationsResponseBody resp1 = client.app().getPerRecordNotifications(req1);
        assertThat(resp1.getRevision()).isEqualTo(revision);
        assertThat(resp1.getNotifications())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(notifications);

        builder.query(number.getCode() + " >= 5").title("n5").user(getDefaultUser());
        notifications.add(perRecordNotification("n5", number.getCode() + " >= 5", t1));
        app.updateRecordNotifications(builder);

        GetPerRecordNotificationsPreviewRequest req2 = new GetPerRecordNotificationsPreviewRequest();
        req2.setApp(app.id());
        req2.setLang("default");
        GetPerRecordNotificationsPreviewResponseBody resp2 =
                client.app().getPerRecordNotificationsPreview(req2);
        assertThat(resp2.getRevision()).isEqualTo(revision + 1);
        assertThat(resp2.getNotifications())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(notifications);
    }

    @Test
    public void getReminderNotifications_getReminderNotificationsPreview() {
        KintoneClient client = setupDefaultClient();
        FieldProperty number = Fields.number();
        FieldProperty userSelect = Fields.userSelect();
        FieldProperty date = Fields.date();
        FieldProperty datetime = Fields.datetime();
        App app = App.create(client, "getReminderNotifications");
        app.addFields(number, userSelect, date, datetime);

        ReminderNotificationsBuilder builder = new ReminderNotificationsBuilder();
        builder.timezone("Asia/Tokyo");
        builder
                .field(datetime, 3, "12:00")
                .title("n1")
                .query(number.getCode() + " >= 1")
                .user(getDefaultUser());
        builder.field(datetime, -2, 1).title("n2").everyone().user(getDefaultUser());
        builder.field(datetime, 1, -3).title("n3").field(userSelect.getCode());
        builder
                .field(date, -5, "23:50")
                .title("n4")
                .org(Orgs.org1.getCode(), true)
                .org(Orgs.org2.getCode(), false);
        app.updateReminderNotifications(builder).deploy();
        long revision = app.getAppRevision(false);

        List<ReminderNotification> notifications = new ArrayList<>();
        ReminderTiming r1 = timingAbsolute(datetime.getCode(), 3, "12:00");
        ReminderTiming r2 = timingRelative(datetime.getCode(), -2, 1);
        ReminderTiming r3 = timingRelative(datetime.getCode(), 1, -3);
        ReminderTiming r4 = timingDate(date.getCode(), -5, "23:50");

        List<NotificationTarget> t1 = makeTargets(EntityType.USER, getDefaultUser());
        List<NotificationTarget> t2 =
                makeTargets(EntityType.GROUP, "everyone", EntityType.USER, getDefaultUser());
        List<NotificationTarget> t3 = makeTargets(EntityType.FIELD_ENTITY, userSelect.getCode());
        List<NotificationTarget> t4 =
                makeTargets(EntityType.ORGANIZATION, Orgs.org1, EntityType.ORGANIZATION, Orgs.org2);
        t4.get(0).setIncludeSubs(true);
        notifications.add(reminderNotification(r1, "n1", number.getCode() + " >= 1", t1));
        notifications.add(reminderNotification(r2, "n2", "", t2));
        notifications.add(reminderNotification(r3, "n3", "", t3));
        notifications.add(reminderNotification(r4, "n4", "", t4));

        GetReminderNotificationsRequest req1 = new GetReminderNotificationsRequest();
        req1.setApp(app.id());
        req1.setLang("default");
        GetReminderNotificationsResponseBody resp1 = client.app().getReminderNotifications(req1);
        assertThat(resp1.getRevision()).isEqualTo(revision);
        assertThat(resp1.getTimezone()).isEqualTo("Asia/Tokyo");
        assertThat(resp1.getNotifications())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(notifications);

        app.updateReminderNotifications(new ReminderNotificationsBuilder().timezone("UTC"));

        GetReminderNotificationsPreviewRequest req2 = new GetReminderNotificationsPreviewRequest();
        req2.setApp(app.id());
        req2.setLang("default");
        GetReminderNotificationsPreviewResponseBody resp2 =
                client.app().getReminderNotificationsPreview(req2);
        assertThat(resp2.getRevision()).isEqualTo(revision + 1);
        assertThat(resp2.getTimezone()).isEqualTo("UTC");
        assertThat(resp2.getNotifications())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(notifications);
    }

    @Test
    public void updateGeneralNotifications() {
        KintoneClient client = setupDefaultClient();
        FieldProperty userSelect = Fields.userSelect();
        App app = App.create(client, "updateGeneralNotifications").addFields(userSelect);
        long revision = app.getAppRevision(true);

        List<GeneralNotification> notifications = new ArrayList<>();
        Entity user = new Entity(EntityType.USER, getDefaultUser());
        Entity group = new Entity(EntityType.GROUP, "everyone");
        Entity org = new Entity(EntityType.ORGANIZATION, Orgs.org1.getCode());
        Entity field = new Entity(EntityType.FIELD_ENTITY, userSelect.getCode());
        notifications.add(generalNotification(user, true, true, true, true, true));
        notifications.add(generalNotification(group, false, false, false, false, false));
        notifications.add(
                generalNotification(org, false, false, false, true, true).setIncludeSubs(true));
        notifications.add(generalNotification(field, true, true, false, false, false));

        UpdateGeneralNotificationsRequest req = new UpdateGeneralNotificationsRequest();
        req.setApp(app.id());
        req.setRevision(revision);
        req.setNotifyToCommenter(true);
        req.setNotifications(notifications);
        UpdateGeneralNotificationsResponseBody resp = client.app().updateGeneralNotifications(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);

        App.GeneralNotifications settings = app.getGeneralNotifications(true);
        assertThat(settings.isNotifyToCommenter()).isTrue();
        assertThat(settings.getNotifications())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(notifications);
    }

    @Test
    public void updatePerRecordNotifications() {
        KintoneClient client = setupDefaultClient();
        FieldProperty number = Fields.number();
        FieldProperty userSelect = Fields.userSelect();
        App app = App.create(client, "updatePerRecordNotifications").addFields(number, userSelect);
        long revision = app.getAppRevision(true);

        List<PerRecordNotification> notifications = new ArrayList<>();
        List<NotificationTarget> t1 = makeTargets(EntityType.USER, getDefaultUser());
        List<NotificationTarget> t2 =
                makeTargets(EntityType.GROUP, "everyone", EntityType.USER, getDefaultUser());
        List<NotificationTarget> t3 =
                makeTargets(EntityType.ORGANIZATION, Orgs.org1, EntityType.ORGANIZATION, Orgs.org2);
        t3.get(0).setIncludeSubs(true);
        List<NotificationTarget> t4 = makeTargets(EntityType.FIELD_ENTITY, userSelect.getCode());
        notifications.add(perRecordNotification("n1", number.getCode() + " >= 1", t1));
        notifications.add(perRecordNotification("n2", number.getCode() + " >= 2", t2));
        notifications.add(perRecordNotification("n3", number.getCode() + " >= 3", t3));
        notifications.add(perRecordNotification("n4", number.getCode() + " >= 4", t4));

        UpdatePerRecordNotificationsRequest req = new UpdatePerRecordNotificationsRequest();
        req.setApp(app.id());
        req.setRevision(revision);
        req.setNotifications(notifications);
        UpdatePerRecordNotificationsResponseBody resp = client.app().updatePerRecordNotifications(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);

        List<PerRecordNotification> settings = app.getRecordNotifications(true);
        assertThat(settings).containsExactly(notifications.toArray(new PerRecordNotification[0]));
    }

    @Test
    public void updateReminderNotifications() {
        KintoneClient client = setupDefaultClient();
        FieldProperty number = Fields.number();
        FieldProperty userSelect = Fields.userSelect();
        FieldProperty date = Fields.date();
        FieldProperty datetime = Fields.datetime();
        App app = App.create(client, "updateReminderNotifications");
        app.addFields(number, userSelect, date, datetime);
        long revision = app.getAppRevision(true);

        List<ReminderNotification> notifications = new ArrayList<>();
        ReminderTiming r1 = timingAbsolute(datetime.getCode(), 3, "12:00");
        ReminderTiming r2 = timingRelative(datetime.getCode(), -2, 1);
        ReminderTiming r3 = timingRelative(datetime.getCode(), 1, -3);
        ReminderTiming r4 = timingDate(date.getCode(), -5, "23:50");

        List<NotificationTarget> t1 = makeTargets(EntityType.USER, getDefaultUser());
        List<NotificationTarget> t2 =
                makeTargets(EntityType.GROUP, "everyone", EntityType.USER, getDefaultUser());
        List<NotificationTarget> t3 = makeTargets(EntityType.FIELD_ENTITY, userSelect.getCode());
        List<NotificationTarget> t4 =
                makeTargets(EntityType.ORGANIZATION, Orgs.org1, EntityType.ORGANIZATION, Orgs.org2);
        t4.get(0).setIncludeSubs(true);
        notifications.add(reminderNotification(r1, "n1", number.getCode() + " >= 1", t1));
        notifications.add(reminderNotification(r2, "n2", "", t2));
        notifications.add(reminderNotification(r3, "n3", "", t3));
        notifications.add(reminderNotification(r4, "n4", "", t4));

        UpdateReminderNotificationsRequest req = new UpdateReminderNotificationsRequest();
        req.setApp(app.id());
        req.setRevision(revision);
        req.setNotifications(notifications);
        req.setTimezone("Asia/Tokyo");
        UpdateReminderNotificationsResponseBody resp = client.app().updateReminderNotifications(req);
        assertThat(resp.getRevision()).isEqualTo(revision + 1);

        App.ReminderNotifications settings = app.getReminderNotifications(true);
        assertThat(settings.getNotifications())
                .containsExactly(notifications.toArray(new ReminderNotification[0]));
        assertThat(settings.getTimezone()).isEqualTo("Asia/Tokyo");
    }

    private GeneralNotification generalNotification(
            Entity entity,
            boolean recordAdded,
            boolean recordEdited,
            boolean commentAdded,
            boolean statusChanged,
            boolean fileImported) {
        GeneralNotification setting = new GeneralNotification();
        setting.setEntity(entity);
        setting.setIncludeSubs(false);
        setting.setRecordAdded(recordAdded);
        setting.setRecordEdited(recordEdited);
        setting.setCommentAdded(commentAdded);
        setting.setStatusChanged(statusChanged);
        setting.setFileImported(fileImported);
        return setting;
    }

    private PerRecordNotification perRecordNotification(
            String title, String query, List<NotificationTarget> targets) {
        PerRecordNotification setting = new PerRecordNotification();
        setting.setTitle(title);
        setting.setFilterCond(query);
        setting.setTargets(targets);
        return setting;
    }

    private ReminderNotification reminderNotification(
            ReminderTiming timing, String title, String query, List<NotificationTarget> targets) {
        ReminderNotification setting = new ReminderNotification();
        setting.setTiming(timing);
        setting.setTitle(title);
        setting.setFilterCond(query);
        setting.setTargets(targets);
        return setting;
    }

    private List<NotificationTarget> makeTargets(Object... args) {
        List<NotificationTarget> targets = new ArrayList<>();
        for (int i = 0; i < args.length; i += 2) {
            EntityType type = (EntityType) args[i];
            Object obj = args[i + 1];
            String code;
            if (obj instanceof UserSetting) {
                code = ((UserSetting) obj).getCode();
            } else if (obj instanceof OrgSetting) {
                code = ((OrgSetting) obj).getCode();
            } else {
                code = (String) obj;
            }
            Entity entity = new Entity(type, code);
            targets.add(new NotificationTarget().setEntity(entity).setIncludeSubs(false));
        }
        return targets;
    }

    private ReminderTiming timingAbsolute(String code, int relativeDays, String time) {
        ReminderTiming t = new ReminderTiming();
        t.setCode(code);
        t.setDaysLater(relativeDays);
        t.setTime(time);
        return t;
    }

    private ReminderTiming timingRelative(String code, int relativeDays, int relativeHours) {
        ReminderTiming t = new ReminderTiming();
        t.setCode(code);
        t.setDaysLater(relativeDays);
        t.setHoursLater(relativeHours);
        return t;
    }

    private ReminderTiming timingDate(String code, int relativeDays, String time) {
        ReminderTiming t = new ReminderTiming();
        t.setCode(code);
        t.setDaysLater(relativeDays);
        t.setTime(time);
        return t;
    }
}
