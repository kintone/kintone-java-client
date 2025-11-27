package com.kintone.client.api.record;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.record.RecordRevision;
import java.util.List;
import lombok.Value;

/** A response object for Update Records API. */
@Value
public class UpdateRecordsResponseBody implements KintoneResponseBody {

    /** A list of objects that include the record IDs and updated revisions. */
    private final List<RecordRevision> records;
}
