package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Get General Notification Settings Preview API. */
@Data
public class GetGeneralNotificationsPreviewRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;
}
