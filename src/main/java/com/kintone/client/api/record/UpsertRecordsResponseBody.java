package com.kintone.client.api.record;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.record.RecordUpsertResult;
import java.util.List;
import lombok.Value;

/** A response object for Upsert Records API. */
@Value
public class UpsertRecordsResponseBody implements KintoneResponseBody {

    /** A list of objects that include the record IDs, updated revisions, and operations. */
    private final List<RecordUpsertResult> records;
}
