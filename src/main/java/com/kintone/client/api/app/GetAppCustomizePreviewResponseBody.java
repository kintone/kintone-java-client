package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.CustomizeBody;
import com.kintone.client.model.app.CustomizeScope;
import lombok.Value;

/** A response object for Get App Customize Preview API. */
@Value
public class GetAppCustomizePreviewResponseBody implements KintoneResponseBody {

    /** The scope of customization * */
    private final CustomizeScope scope;

    /** An object containing data of JavaScript and CSS files for the desktop. */
    private final CustomizeBody desktop;

    /** An object containing data of JavaScript and CSS files for the mobile. */
    private final CustomizeBody mobile;

    /** The revision number of the app settings. */
    private final long revision;
}
