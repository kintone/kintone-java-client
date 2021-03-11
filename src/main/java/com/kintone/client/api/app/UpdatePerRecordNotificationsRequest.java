package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.app.PerRecordNotification;
import java.util.List;
import lombok.Data;

/** A request object for Update Per Record Notification Settings API. */
@Data
public class UpdatePerRecordNotificationsRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /**
    * An array of objects containing data of the Per Record Notification settings (optional).
    *
    * <ul>
    *   <li>If the array is empty, all of the Per Record Notification settings will be deleted.
    *   <li>If ignored, this setting will not be changed.
    * </ul>
    */
    private List<PerRecordNotification> notifications;

    /**
    * The expected revision number of the App settings (optional). The request will fail if the
    * revision number is not the latest revision. The revision will not be checked if this parameter
    * is null, or -1 is specified.
    */
    private Long revision;
}
