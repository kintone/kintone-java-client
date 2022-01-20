package com.kintone.client.api.record;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.Order;
import lombok.Data;

/** A request object for Get Record Comments API. */
@Data
public class GetRecordCommentsRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** The record ID (required). */
    private Long record;

    /** The sort order of the comment ID (optional). */
    private Order order;

    /**
     * The offset of comments to retrieve (optional). This skips the retrieval of the first number of
     * comments.
     */
    private Long offset;

    /** The number of records to retrieve (optional). */
    private Long limit;
}
