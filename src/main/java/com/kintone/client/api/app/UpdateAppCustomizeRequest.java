package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.app.CustomizeBody;
import com.kintone.client.model.app.CustomizeScope;
import lombok.Data;

/** A request object for Update App Customize API. */
@Data
public class UpdateAppCustomizeRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** The scope of customization (optional). If set to null, leaves this setting unchanged. */
    private CustomizeScope scope;

    /**
    * An object containing data of JavaScript and CSS files for the desktop (optional). If set to
    * null, leaves this setting unchanged.
    */
    private CustomizeBody desktop;

    /**
    * An object containing data of JavaScript and CSS files for the mobile (optional). If set to
    * null, leaves this setting unchanged.
    */
    private CustomizeBody mobile;

    /**
    * The expected revision number of the App settings (optional). The request will fail if the
    * revision number is not the latest revision. The revision will not be checked if this parameter
    * is null, or -1 is specified.
    */
    private Long revision;
}
