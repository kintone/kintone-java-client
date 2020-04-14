package com.kintone.client.api.space;

import com.kintone.client.api.KintoneResponseBody;
import lombok.Value;

/** A response object for Add Space From Template API. */
@Value
public class AddSpaceFromTemplateResponseBody implements KintoneResponseBody {

    /** The Space ID of the created Space. */
    private final long id;
}
