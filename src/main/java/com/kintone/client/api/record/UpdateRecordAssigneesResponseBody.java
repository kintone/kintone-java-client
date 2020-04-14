package com.kintone.client.api.record;

import com.kintone.client.api.KintoneResponseBody;
import lombok.Value;

/** A response object for Update Record Assignees API. */
@Value
public class UpdateRecordAssigneesResponseBody implements KintoneResponseBody {

    /** The revision number of the record after updating the Assignees. */
    private final long revision;
}
