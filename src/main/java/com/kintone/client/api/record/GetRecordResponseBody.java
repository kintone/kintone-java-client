package com.kintone.client.api.record;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.record.Record;
import lombok.Value;

/** A response object for Get Record API. */
@Value
public class GetRecordResponseBody implements KintoneResponseBody {

    /** The record object. */
    private Record record;
}
