package com.kintone.client.api.record;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.record.RecordForUpdate;
import java.util.List;
import lombok.Data;

/** A request object for Update Records API. */
@Data
public class UpdateRecordsRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** A list of objects that include id/updateKey, revision and record objects (required). */
    private List<RecordForUpdate> records;
}
