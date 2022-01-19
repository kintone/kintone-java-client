package com.kintone.client.api.record;

import com.kintone.client.api.KintoneRequest;
import java.util.List;
import lombok.Data;

/** A request object for Delete Records API. */
@Data
public class DeleteRecordsRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** A list of record IDs that will be deleted (required). */
    private List<Long> ids;

    /**
     * The expected revision numbers (optional). The first id number will correspond to the first
     * revision number in the list, the second id to the second revision number, and so on. If a
     * element is null or -1, the revision number will not be checked for the corresponding record ID.
     * Set to null to ignore the revisions for all deleting records.
     */
    private List<Long> revisions;
}
