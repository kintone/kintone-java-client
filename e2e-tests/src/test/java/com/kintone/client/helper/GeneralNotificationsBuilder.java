package com.kintone.client.helper;

import com.kintone.client.api.app.UpdateGeneralNotificationsRequest;
import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import com.kintone.client.model.app.GeneralNotification;
import java.util.ArrayList;
import java.util.List;

public class GeneralNotificationsBuilder {
    private List<GeneralNotification> notifications;
    private Boolean notifyToCommenter;

    private GeneralNotification getCurrent() {
        return notifications.get(notifications.size() - 1);
    }

    private void addNotification(GeneralNotification setting) {
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        notifications.add(setting);
    }

    public GeneralNotificationsBuilder user(String code) {
        GeneralNotification n = new GeneralNotification();
        n.setEntity(new Entity(EntityType.USER, code));
        n.setIncludeSubs(false);
        addNotification(n);
        return this;
    }

    public GeneralNotificationsBuilder group(String code) {
        GeneralNotification n = new GeneralNotification();
        n.setEntity(new Entity(EntityType.GROUP, code));
        n.setIncludeSubs(false);
        addNotification(n);
        return this;
    }

    public GeneralNotificationsBuilder org(String code, boolean includeSubs) {
        GeneralNotification n = new GeneralNotification();
        n.setEntity(new Entity(EntityType.ORGANIZATION, code));
        n.setIncludeSubs(includeSubs);
        addNotification(n);
        return this;
    }

    public GeneralNotificationsBuilder field(String code) {
        GeneralNotification n = new GeneralNotification();
        n.setEntity(new Entity(EntityType.FIELD_ENTITY, code));
        n.setIncludeSubs(false);
        addNotification(n);
        return this;
    }

    public GeneralNotificationsBuilder everyone() {
        return group("everyone");
    }

    public GeneralNotificationsBuilder recordAdded(boolean flag) {
        getCurrent().setRecordAdded(flag);
        return this;
    }

    public GeneralNotificationsBuilder recordEdited(boolean flag) {
        getCurrent().setRecordEdited(flag);
        return this;
    }

    public GeneralNotificationsBuilder commentAdded(boolean flag) {
        getCurrent().setCommentAdded(flag);
        return this;
    }

    public GeneralNotificationsBuilder statusChanged(boolean flag) {
        getCurrent().setStatusChanged(flag);
        return this;
    }

    public GeneralNotificationsBuilder fileImported(boolean flag) {
        getCurrent().setFileImported(flag);
        return this;
    }

    public GeneralNotificationsBuilder all(boolean flag) {
        getCurrent().setRecordAdded(flag);
        getCurrent().setRecordEdited(flag);
        getCurrent().setCommentAdded(flag);
        getCurrent().setStatusChanged(flag);
        getCurrent().setFileImported(flag);
        return this;
    }

    public GeneralNotificationsBuilder notifyToCommenter(boolean flag) {
        notifyToCommenter = flag;
        return this;
    }

    UpdateGeneralNotificationsRequest build(long appId) {
        UpdateGeneralNotificationsRequest req = new UpdateGeneralNotificationsRequest();
        req.setApp(appId);
        req.setNotifications(notifications);
        req.setNotifyToCommenter(notifyToCommenter);
        return req;
    }
}
