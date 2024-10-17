package com.kintone.client.api.plugin;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.plugin.Plugin;
import java.util.List;
import lombok.Value;

/** A response object for Get Required Plug-ins API. */
@Value
public class GetRequiredPluginsResponseBody implements KintoneResponseBody {

    /** A list of Plug-ins that needs to be installed. */
    private final List<Plugin> plugins;
}
