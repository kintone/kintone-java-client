package com.kintone.client.api.plugin;

import com.kintone.client.api.KintoneResponseBody;
import lombok.Value;

/** A response object for Install Plug-in API. */
@Value
public class InstallPluginResponseBody implements KintoneResponseBody {

    /** The installed Plug-in ID. */
    private final String id;

    /** The version number of the Plug-in. */
    private final String version;
}
