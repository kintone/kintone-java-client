package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Get Reminder Notification Settings Preview API. */
@Data
public class GetReminderNotificationsPreviewRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /**
     * The localization language setting (optional). If set to null, the default names will be
     * retrieved.
     */
    private String lang;
}
