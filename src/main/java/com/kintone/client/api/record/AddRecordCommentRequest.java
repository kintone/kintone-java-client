package com.kintone.client.api.record;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.record.RecordComment;
import lombok.Data;

/** A request object for Add Record Comment API. */
@Data
public class AddRecordCommentRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** The Record ID (required). */
    private Long record;

    /** An object including comment details (required). */
    private RecordComment comment;
}
