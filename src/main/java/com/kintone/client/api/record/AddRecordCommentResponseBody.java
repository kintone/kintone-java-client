package com.kintone.client.api.record;

import com.kintone.client.api.KintoneResponseBody;
import lombok.Value;

/** A response object for Add Record Comment API. */
@Value
public class AddRecordCommentResponseBody implements KintoneResponseBody {

    /** The Comment ID. */
    private final long id;
}
