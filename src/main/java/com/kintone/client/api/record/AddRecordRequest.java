package com.kintone.client.api.record;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.record.Record;
import lombok.Data;

/** A request object for Add Record API. */
@Data
public class AddRecordRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /**
    * The record object. Field codes and values are specified in this object. If set to null, the
    * record will be added with default field values.
    */
    private Record record;
}
