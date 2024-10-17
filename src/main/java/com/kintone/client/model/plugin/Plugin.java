package com.kintone.client.model.plugin;

import lombok.Value;

/** General information of a Plugin retrieved by Get Plugins API. */
@Value
public class Plugin {

    /** The Plugin ID. */
    private final String id;

    /** The name of the Plugin. */
    private final String name;

    /** States whether or not the plug-in is a Marketplace plug-in. */
    private final boolean isMarketPlugin;

    /** The version number of the plug-in. */
    private final String version;
}
