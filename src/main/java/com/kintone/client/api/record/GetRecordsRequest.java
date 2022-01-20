package com.kintone.client.api.record;

import com.kintone.client.api.KintoneRequest;
import java.util.List;
import lombok.Data;

/** A request object for Get Records API. */
@Data
public class GetRecordsRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** The field codes to be included in the response (optional). */
    private List<String> fields;

    /** The query string that specifies what records to include in the response (optional). */
    private String query;

    /**
     * If set to true, the total count of records that match the query conditions will be included in
     * the response (optional).
     */
    private Boolean totalCount;
}
