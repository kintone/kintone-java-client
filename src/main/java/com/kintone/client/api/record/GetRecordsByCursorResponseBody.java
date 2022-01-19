package com.kintone.client.api.record;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.record.Record;
import java.util.List;
import lombok.Value;

/** A response object for Get Records By Cursor API. */
@Value
public class GetRecordsByCursorResponseBody implements KintoneResponseBody {

    /** States whether there are more records that can be acquired from the cursor. */
    private final boolean next;

    /** A list of record objects. */
    private final List<Record> records;

    /**
     * Tests whether there are more records that can be acquired from the cursor.
     *
     * @return true if there are more records that can be acquired from the cursor.
     */
    public boolean hasNext() {
        return this.next;
    }
}
