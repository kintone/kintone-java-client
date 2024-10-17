package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Move App to Space API. */
@Data
public class MoveAppRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /**
     * The Space ID of where the App will be moved to (required). To remove an App from its current
     * space, null can be specified.
     */
    private Long space;
}
