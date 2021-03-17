package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.ReminderNotification;
import java.util.List;
import lombok.Value;

/** A response object for Get Reminder Notification Settings API. */
@Value
public class GetReminderNotificationsResponseBody implements KintoneResponseBody {

    /**
    * An array of objects consisting of "Notification Trigger" options. These options define when the
    * notifications will be sent out, under what conditions, and the recipients.
    */
    private final List<ReminderNotification> notifications;

    /**
    * The timezone that determines the Reminder notification's timing. This reflects the "Reminder
    * Time Zone" dropdown option.
    */
    private String timezone;

    /** The revision number of the App settings. */
    private final long revision;
}
