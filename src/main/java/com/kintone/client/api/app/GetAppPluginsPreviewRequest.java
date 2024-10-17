package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Get App Plug-ins Preview API. */
@Data
public class GetAppPluginsPreviewRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /**
     * The localization language setting (optional). If set to null, the display language for the user
     * executing the API is applied.
     */
    private String lang;
}
