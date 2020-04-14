package com.kintone.client.model.app;

import lombok.Value;

/** Created or updated view ID returned by Update Views API. */
@Value
public class ViewId {

    /** The View ID. */
    private final long id;
}
