package com.kintone.client.model.app;

/** The scope of JavaScript and CSS customization. */
public enum CustomizeScope {

    /** The customization affects all users. */
    ALL,

    /** The customization affects only App administrators. */
    ADMIN,

    /** Disabled. The customization does not affect anyone. */
    NONE
}
