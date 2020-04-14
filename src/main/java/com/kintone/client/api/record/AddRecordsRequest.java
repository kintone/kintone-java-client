package com.kintone.client.api.record;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.record.Record;
import java.util.List;
import lombok.Data;

/** A request object for Add Records API. */
@Data
public class AddRecordsRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** A list of record objects, that contains field codes and their values (required). */
    private List<Record> records;
}
