package com.kintone.client.api.record;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Get Records By Cursor API. */
@Data
public class GetRecordsByCursorRequest implements KintoneRequest {

    /** The cursor ID (required). */
    private String id;
}
