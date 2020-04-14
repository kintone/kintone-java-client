package com.kintone.client.api.record;

import com.kintone.client.api.KintoneResponseBody;
import lombok.Value;

/** A response object for Update Record API. */
@Value
public class UpdateRecordResponseBody implements KintoneResponseBody {

    /** The revision number of the record. */
    private final long revision;
}
