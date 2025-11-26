package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Move App to Space API. */
@Data
public class MoveAppRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /**
     * The Space ID of where the App will be moved to (optional). If set to null or not set, the App
     * will be removed from its current space.
     */
    private Long space;
}
