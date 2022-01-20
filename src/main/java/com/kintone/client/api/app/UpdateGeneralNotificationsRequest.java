package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.app.GeneralNotification;
import java.util.List;
import lombok.Data;

/** A request object for Update General Notification Settings API. */
@Data
public class UpdateGeneralNotificationsRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /**
     * An array of objects consisting of "Recipients and Conditions" options (optional).
     *
     * <ul>
     *   <li>If an empty array is sent, all the recipients will be removed.
     *   <li>If this parameter is ignored, no changes will be made to the "Recipients and Conditions"
     *       options.
     * </ul>
     */
    private List<GeneralNotification> notifications;

    /**
     * Option to notify all commenters of a record when a comment is posted on that record (optional).
     * This reflects the "Send updated comment notifications to all commenters" checkbox.
     *
     * <ul>
     *   <li>true : Notify all commenters of the record when a comment is posted
     *   <li>false : Do not notify all commenters of the record when a comment is posted
     * </ul>
     *
     * If ignored, this setting will not be changed.
     */
    private Boolean notifyToCommenter;

    /**
     * The expected revision number of the App settings (optional). The request will fail if the
     * revision number is not the latest revision. The revision will not be checked if this parameter
     * is null, or -1 is specified.
     */
    private Long revision;
}
