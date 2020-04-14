package com.kintone.client.api.record;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Delete Cursor API. */
@Data
public class DeleteCursorRequest implements KintoneRequest {

    /** The cursor ID (required). */
    private String id;
}
