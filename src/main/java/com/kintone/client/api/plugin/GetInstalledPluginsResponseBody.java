package com.kintone.client.api.plugin;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.plugin.Plugin;
import java.util.List;
import lombok.Value;

/** A response object for Get Installed Plugins API. */
@Value
public class GetInstalledPluginsResponseBody implements KintoneResponseBody {

    /** A list of Plug-ins added to the App. */
    private final List<Plugin> plugins;
}
