package com.kintone.client.api.space;

import com.kintone.client.api.KintoneResponseBody;
import lombok.Value;

/** A response object for Add Thread Comment API. */
@Value
public class AddThreadCommentResponseBody implements KintoneResponseBody {

    /** The comment ID of the created comment. */
    private final long id;
}
