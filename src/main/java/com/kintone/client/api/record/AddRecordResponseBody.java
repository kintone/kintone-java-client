package com.kintone.client.api.record;

import com.kintone.client.api.KintoneResponseBody;
import lombok.Value;

/** A response object for Add Record API. */
@Value
public class AddRecordResponseBody implements KintoneResponseBody {

    /** The Record ID of the created Record. */
    private final long id;

    /** The revision number of the Record. */
    private final long revision;
}
