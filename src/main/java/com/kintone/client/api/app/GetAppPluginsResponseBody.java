package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.AppPlugin;
import java.util.List;
import lombok.Value;

/** A response object for Get App Plug-ins API. */
@Value
public class GetAppPluginsResponseBody implements KintoneResponseBody {

    /** A list of Plug-ins added to the App. * */
    private final List<AppPlugin> plugins;

    /** The revision number of the app settings. */
    private final long revision;
}
