package com.kintone.client.helper;

import com.kintone.client.api.app.UpdatePerRecordNotificationsRequest;
import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import com.kintone.client.model.app.NotificationTarget;
import com.kintone.client.model.app.PerRecordNotification;
import java.util.ArrayList;
import java.util.List;

public class RecordNotificationsBuilder {
    private List<PerRecordNotification> notifications;

    private PerRecordNotification getCurrent() {
        return notifications.get(notifications.size() - 1);
    }

    public RecordNotificationsBuilder query(String query) {
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        PerRecordNotification n = new PerRecordNotification();
        n.setFilterCond(query);
        n.setTargets(new ArrayList<>());
        notifications.add(n);
        return this;
    }

    public RecordNotificationsBuilder title(String title) {
        getCurrent().setTitle(title);
        return this;
    }

    public RecordNotificationsBuilder user(String code) {
        Entity e = new Entity(EntityType.USER, code);
        NotificationTarget t = new NotificationTarget().setEntity(e).setIncludeSubs(false);
        getCurrent().getTargets().add(t);
        return this;
    }

    public RecordNotificationsBuilder group(String code) {
        Entity e = new Entity(EntityType.GROUP, code);
        NotificationTarget t = new NotificationTarget().setEntity(e).setIncludeSubs(false);
        getCurrent().getTargets().add(t);
        return this;
    }

    public RecordNotificationsBuilder org(String code, boolean includeSubs) {
        Entity e = new Entity(EntityType.ORGANIZATION, code);
        NotificationTarget t = new NotificationTarget().setEntity(e).setIncludeSubs(includeSubs);
        getCurrent().getTargets().add(t);
        return this;
    }

    public RecordNotificationsBuilder field(String code) {
        Entity e = new Entity(EntityType.FIELD_ENTITY, code);
        NotificationTarget t = new NotificationTarget().setEntity(e).setIncludeSubs(false);
        getCurrent().getTargets().add(t);
        return this;
    }

    public RecordNotificationsBuilder everyone() {
        return group("everyone");
    }

    UpdatePerRecordNotificationsRequest build(long appId) {
        UpdatePerRecordNotificationsRequest req = new UpdatePerRecordNotificationsRequest();
        req.setApp(appId);
        req.setNotifications(notifications);
        return req;
    }
}
