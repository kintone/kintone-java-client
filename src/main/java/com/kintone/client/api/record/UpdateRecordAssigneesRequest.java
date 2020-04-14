package com.kintone.client.api.record;

import com.kintone.client.api.KintoneRequest;
import java.util.List;
import lombok.Data;

/** A request object for Update Record Assignees API. */
@Data
public class UpdateRecordAssigneesRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** The record ID (required). */
    private Long id;

    /** The user code(s) (log in names) of the Assignee(s) (required). */
    private List<String> assignees;

    /**
    * The expected revision number of the record before updating the Assignees (optional). If the
    * specified revision is not the latest revision, the request will result in an error. The
    * revision will not be checked if this parameter is null or -1.
    */
    private Long revision;
}
