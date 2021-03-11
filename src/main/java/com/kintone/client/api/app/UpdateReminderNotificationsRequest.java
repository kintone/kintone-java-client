package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.app.ReminderNotification;
import java.util.List;
import lombok.Data;

/** A request object for Update Reminder Notification Settings API. */
@Data
public class UpdateReminderNotificationsRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /**
    * An array of objects consisting of "Notification Trigger" options (optional). These options
    * define when the notifications will be sent out, under what conditions, and the recipients.
    *
    * <ul>
    *   <li>If an empty array is sent, all the recipients will be removed.
    *   <li>If this parameter is ignored, no changes will be made to the "Notification Trigger"
    *       options.
    * </ul>
    */
    private List<ReminderNotification> notifications;

    /**
    * The timezone that determines the Reminder notification's timing (optional). This reflects the
    * "Reminder Time Zone" dropdown option.
    *
    * <p>If the App's Reminder Notification settings have never been configured, the API executing
    * user's timezone will be used.
    */
    private String timezone;

    /**
    * The expected revision number of the App settings (optional). The request will fail if the
    * revision number is not the latest revision. The revision will not be checked if this parameter
    * is null, or -1 is specified.
    */
    private Long revision;
}
