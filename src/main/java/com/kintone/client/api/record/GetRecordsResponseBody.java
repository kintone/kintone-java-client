package com.kintone.client.api.record;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.record.Record;
import java.util.List;
import lombok.Value;

/** A response object for Get Records API. */
@Value
public class GetRecordsResponseBody implements KintoneResponseBody {

    /** A list of record objects. */
    private final List<Record> records;

    /** The total count of records that match the query conditions. */
    private final Long totalCount;
}
