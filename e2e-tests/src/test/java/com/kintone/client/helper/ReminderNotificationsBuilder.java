package com.kintone.client.helper;

import com.kintone.client.api.app.UpdateReminderNotificationsRequest;
import com.kintone.client.model.Entity;
import com.kintone.client.model.EntityType;
import com.kintone.client.model.app.NotificationTarget;
import com.kintone.client.model.app.ReminderNotification;
import com.kintone.client.model.app.ReminderTiming;
import com.kintone.client.model.app.field.FieldProperty;
import com.kintone.client.model.record.FieldType;
import java.util.ArrayList;
import java.util.List;

public class ReminderNotificationsBuilder {
    private List<ReminderNotification> notifications;
    private String timezone;

    private ReminderNotification getCurrent() {
        return notifications.get(notifications.size() - 1);
    }

    public ReminderNotificationsBuilder datetime(String code, int daysLater, String time) {
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        ReminderNotification n = new ReminderNotification();
        ReminderTiming timing =
                new ReminderTiming().setCode(code).setDaysLater(daysLater).setTime(time);
        n.setTiming(timing);
        n.setTargets(new ArrayList<>());
        notifications.add(n);
        return this;
    }

    public ReminderNotificationsBuilder datetime(String code, int daysLater, int hoursLater) {
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        ReminderNotification n = new ReminderNotification();
        ReminderTiming timing =
                new ReminderTiming().setCode(code).setDaysLater(daysLater).setHoursLater(hoursLater);
        n.setTiming(timing);
        n.setTargets(new ArrayList<>());
        notifications.add(n);
        return this;
    }

    public ReminderNotificationsBuilder date(String code, int daysLater, String time) {
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        ReminderNotification n = new ReminderNotification();
        ReminderTiming timing =
                new ReminderTiming().setCode(code).setDaysLater(daysLater).setTime(time);
        n.setTiming(timing);
        n.setTargets(new ArrayList<>());
        notifications.add(n);
        return this;
    }

    public ReminderNotificationsBuilder field(FieldProperty field, int daysLater, String time) {
        if (field.getType() == FieldType.DATETIME
                || field.getType() == FieldType.CREATED_TIME
                || field.getType() == FieldType.UPDATED_TIME) {
            return datetime(field.getCode(), daysLater, time);
        }
        if (field.getType() == FieldType.DATE) {
            return date(field.getCode(), daysLater, time);
        }
        throw new AssertionError("invalid field type: " + field.getType());
    }

    public ReminderNotificationsBuilder field(FieldProperty field, int daysLater, int hoursLater) {
        if (field.getType() == FieldType.DATETIME
                || field.getType() == FieldType.CREATED_TIME
                || field.getType() == FieldType.UPDATED_TIME) {
            return datetime(field.getCode(), daysLater, hoursLater);
        }
        throw new AssertionError("invalid field type: " + field.getType());
    }

    public ReminderNotificationsBuilder title(String title) {
        getCurrent().setTitle(title);
        return this;
    }

    public ReminderNotificationsBuilder query(String query) {
        getCurrent().setFilterCond(query);
        return this;
    }

    public ReminderNotificationsBuilder user(String code) {
        Entity e = new Entity(EntityType.USER, code);
        NotificationTarget t = new NotificationTarget().setEntity(e).setIncludeSubs(false);
        getCurrent().getTargets().add(t);
        return this;
    }

    public ReminderNotificationsBuilder group(String code) {
        Entity e = new Entity(EntityType.GROUP, code);
        NotificationTarget t = new NotificationTarget().setEntity(e).setIncludeSubs(false);
        getCurrent().getTargets().add(t);
        return this;
    }

    public ReminderNotificationsBuilder org(String code, boolean includeSubs) {
        Entity e = new Entity(EntityType.ORGANIZATION, code);
        NotificationTarget t = new NotificationTarget().setEntity(e).setIncludeSubs(includeSubs);
        getCurrent().getTargets().add(t);
        return this;
    }

    public ReminderNotificationsBuilder field(String code) {
        Entity e = new Entity(EntityType.FIELD_ENTITY, code);
        NotificationTarget t = new NotificationTarget().setEntity(e).setIncludeSubs(false);
        getCurrent().getTargets().add(t);
        return this;
    }

    public ReminderNotificationsBuilder everyone() {
        return group("everyone");
    }

    public ReminderNotificationsBuilder timezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    UpdateReminderNotificationsRequest build(long appId) {
        UpdateReminderNotificationsRequest req = new UpdateReminderNotificationsRequest();
        req.setApp(appId);
        req.setNotifications(notifications);
        req.setTimezone(timezone);
        return req;
    }
}
