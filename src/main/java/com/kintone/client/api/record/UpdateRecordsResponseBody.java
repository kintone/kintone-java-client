package com.kintone.client.api.record;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.record.RecordUpdateResult;
import java.util.List;
import lombok.Value;

/** A response object for Update Records API. */
@Value
public class UpdateRecordsResponseBody implements KintoneResponseBody {

    /** A list of objects that include the record IDs, updated revisions, and operations. */
    private final List<RecordUpdateResult> records;
}
