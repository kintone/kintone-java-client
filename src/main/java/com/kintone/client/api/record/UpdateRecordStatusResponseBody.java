package com.kintone.client.api.record;

import com.kintone.client.api.KintoneResponseBody;
import lombok.Value;

/** A response object for Update Record Status API. */
@Value
public class UpdateRecordStatusResponseBody implements KintoneResponseBody {

    /** The revision number of the record after updating the status. */
    private final long revision;
}
