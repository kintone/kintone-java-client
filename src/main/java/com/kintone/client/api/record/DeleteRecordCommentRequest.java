package com.kintone.client.api.record;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Delete Record Comment API. */
@Data
public class DeleteRecordCommentRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** The record ID (required). */
    private Long record;

    /** The Comment ID (required). */
    private Long comment;
}
