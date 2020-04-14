package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Add App API. */
@Data
public class AddAppRequest implements KintoneRequest {

    /** The App name (required). */
    private String name;

    /**
    * The Space ID of where the App will be created (optional). If set to null, the App will not
    * attached to any Space.
    */
    private Long space;

    /**
    * The Thread ID of the thread in the Space where the App will be created. This parameter is
    * required if the "space" parameter is not null.
    */
    private Long thread;
}
