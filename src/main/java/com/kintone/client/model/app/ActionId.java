package com.kintone.client.model.app;

import lombok.Value;

/** Created or updated Action ID returned by Update App Action Settings API. */
@Value
public class ActionId {

    /** The ID of the Action. */
    private final long id;
}
