package com.kintone.client.api.record;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.record.RecordForUpdate;
import java.util.List;
import lombok.Data;

/** A request object for Upsert Records API. */
@Data
public class UpsertRecordsRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** A list of objects that include id/updateKey, revision and record objects (required). */
    private List<RecordForUpdate> records;

    /**
     * If set to true, the UPSERT mode will be enabled. In UPSERT mode, if the specified record
     * exists, it will be updated. If it does not exist, a new record will be created (optional).
     */
    private final Boolean upsert = true;
}
