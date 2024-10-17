package com.kintone.client.model.app;

import lombok.Value;

/** An object containing the information of a Plug-in added to an App. */
@Value
public class AppPlugin {

    /** The Plug-in ID. */
    private String id;

    /** The name of the Plug-in. */
    private String name;

    /** The status of the Plug-in. true for active Plug-in, and false for inactive Plug-in. */
    private boolean enabled;
}
