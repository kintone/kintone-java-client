package com.kintone.client.api.record;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Update Record Status API. */
@Data
public class UpdateRecordStatusRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** The record ID (required). */
    private Long id;

    /** The Action name of the action to run (required). */
    private String action;

    /** The next Assignee (optional). */
    private String assignee;

    /**
     * The expected revision number of the record before updating the status (optional). If the
     * specified revision is not the latest revision, the request will result in an error. The
     * revision will not be checked if this parameter is null or -1.
     */
    private Long revision;
}
