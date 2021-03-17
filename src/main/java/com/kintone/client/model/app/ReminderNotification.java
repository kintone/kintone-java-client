package com.kintone.client.model.app;

import java.util.List;
import lombok.Data;

/** An object representing the reminder notification settings. */
@Data
public class ReminderNotification {

    /** The notification subject. */
    private String title;

    /** The record's filter condition in query string format. */
    private String filterCond;

    /** An object containing the Reminder notification's timing. */
    private ReminderTiming timing;

    /** An array of objects containing the recipients of the Reminder Notification. */
    private List<NotificationTarget> targets;
}
