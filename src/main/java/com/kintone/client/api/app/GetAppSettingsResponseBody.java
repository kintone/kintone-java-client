package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.AppIcon;
import lombok.Value;

/** A response object for Get App Settings API. */
@Value
public class GetAppSettingsResponseBody implements KintoneResponseBody {

    /** The App name. */
    private final String name;

    /** The app description in HTML format. */
    private final String description;

    /** An object containing data of the App icon. */
    private final AppIcon icon;

    /** The color theme. */
    private final String theme;

    /** The revision number of the App settings. */
    private final long revision;
}
