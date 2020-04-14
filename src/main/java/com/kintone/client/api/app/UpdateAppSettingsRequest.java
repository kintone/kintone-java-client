package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.app.AppIcon;
import lombok.Data;

/** A request object for Update App Settings API. */
@Data
public class UpdateAppSettingsRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** The App name (optional). If set to null, leaves this setting unchanged. */
    private String name;

    /** The App description (optional). If set to null, leaves this setting unchanged. */
    private String description;

    /**
    * An object containing information of the App icon (optional). If set to null, leaves this
    * setting unchanged.
    */
    private AppIcon icon;

    /** The color theme (optional). If set to null, leaves this setting unchanged. */
    private String theme;

    /**
    * The expected revision number of the App settings (optional). The request will fail if the
    * revision number is not the latest revision. The revision will not be checked if this parameter
    * is null, or -1 is specified.
    */
    private Long revision;
}
