package com.kintone.client.model.app;

import java.util.List;
import lombok.Data;

/** An object representing the per record notification settings. */
@Data
public class PerRecordNotification {

    /** The notification subject. */
    private String title;

    /** The record's filter condition in query string format. */
    private String filterCond;

    /** An array of objects containing the recipients of the Per Record Notification. */
    private List<NotificationTarget> targets;
}
