package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.PerRecordNotification;
import java.util.List;
import lombok.Data;

/** A response object for Get Per Record Notification Settings Preview API. */
@Data
public class GetPerRecordNotificationsPreviewResponseBody implements KintoneResponseBody {

    /** An array of objects containing data of the Per Record Notification settings. */
    private final List<PerRecordNotification> notifications;

    /** The revision number of the App settings. */
    private final long revision;
}
