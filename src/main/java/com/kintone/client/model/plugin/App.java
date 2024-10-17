package com.kintone.client.model.plugin;

import lombok.Value;

/** General information of an App retrieved by Get Apps API. */
@Value
public class App {

    /** The App ID. */
    private final long id;

    /** The name of the App. */
    private final String name;
}
