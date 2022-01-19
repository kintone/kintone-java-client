package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.GeneralNotification;
import java.util.List;
import lombok.Value;

/** A response object for Get General Notification Settings API. */
@Value
public class GetGeneralNotificationsResponseBody implements KintoneResponseBody {

    /**
     * An array of objects consisting of "Recipients and Conditions" options. These options define who
     * will receive the notifications.
     */
    private final List<GeneralNotification> notifications;

    /**
     * Option to notify all commenters of a record when a comment is posted on that record. This
     * reflects the "Send updated comment notifications to all commenters" checkbox.
     *
     * <ul>
     *   <li>true : Notify all commenters of the record when a comment is posted
     *   <li>false : Do not notify all commenters of the record when a comment is posted
     * </ul>
     */
    private final boolean notifyToCommenter;

    /** The revision number of the App settings. */
    private final long revision;
}
