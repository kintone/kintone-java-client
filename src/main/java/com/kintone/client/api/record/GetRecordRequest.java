package com.kintone.client.api.record;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Get Record API. */
@Data
public class GetRecordRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** The record ID (required). */
    private Long id;
}
