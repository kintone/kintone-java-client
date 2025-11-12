package com.kintone.client.model.app;

import lombok.Value;

/** Space information of an App. */
@Value
public class AppSpace {

    /** The Space ID. */
    private final Long id;

    /** The Space name. */
    private final String name;
}
