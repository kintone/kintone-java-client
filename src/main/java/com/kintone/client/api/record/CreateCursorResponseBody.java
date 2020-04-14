package com.kintone.client.api.record;

import com.kintone.client.api.KintoneResponseBody;
import lombok.Value;

/** A response object for Create Cursor API. */
@Value
public class CreateCursorResponseBody implements KintoneResponseBody {

    /** The cursor ID. */
    private final String id;

    /** The total count of records that match the query conditions. */
    private final long totalCount;
}
