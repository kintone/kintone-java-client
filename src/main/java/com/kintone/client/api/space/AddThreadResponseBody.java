package com.kintone.client.api.space;

import com.kintone.client.api.KintoneResponseBody;
import lombok.Value;

/** A response object for Add Thread API. */
@Value
public class AddThreadResponseBody implements KintoneResponseBody {

    /** The Thread ID of the created Thread. */
    private final long id;
}
