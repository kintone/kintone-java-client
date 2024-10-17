package com.kintone.client.api.plugin;

import com.kintone.client.api.KintoneResponseBody;
import lombok.Value;

/** A response object for Update Plug-in API. */
@Value
public class UpdatePluginResponseBody implements KintoneResponseBody {

    /** The Plug-in ID of the updated Plug-in. */
    private final String id;

    /** The version number of the Plug-in. */
    private final String version;
}
