package com.kintone.client.api.record;

import com.kintone.client.api.KintoneResponseBody;
import java.util.List;
import lombok.Value;

/** A response object for Add Records API. */
@Value
public class AddRecordsResponseBody implements KintoneResponseBody {

    /** The Record IDs of the created records. */
    private final List<Long> ids;

    /** The revision numbers of the records. */
    private final List<Long> revisions;
}
