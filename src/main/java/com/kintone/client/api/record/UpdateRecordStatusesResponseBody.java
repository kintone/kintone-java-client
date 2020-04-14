package com.kintone.client.api.record;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.record.RecordRevision;
import java.util.List;
import lombok.Value;

/** A response object for Update Record Statuses API. */
@Value
public class UpdateRecordStatusesResponseBody implements KintoneResponseBody {

    /** A list of objects including information of the updated records. */
    private final List<RecordRevision> records;
}
