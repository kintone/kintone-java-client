package com.kintone.client.api.record;

import com.kintone.client.api.KintoneRequest;
import java.util.List;
import lombok.Data;

/** A request object for Create Cursor API. */
@Data
public class CreateCursorRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** The field codes to be included in the response when using the Get Cursor API (optional). */
    private List<String> fields;

    /**
     * The query string (optional). This parameter will specify what records will be responded when
     * using the Get Cursor API.
     */
    private String query;

    /**
     * The maximum number of records the Get Cursor API can retrieve from this cursor with one request
     * (optional).
     */
    private Long size;
}
