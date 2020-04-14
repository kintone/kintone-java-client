package com.kintone.client.api.record;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.record.Record;
import com.kintone.client.model.record.UpdateKey;
import lombok.Data;

/** A request object for Update Record API. */
@Data
public class UpdateRecordRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** The Record ID of the record to be updated. Either "id" or "updateKey" is required. */
    private Long id;

    /** The unique key of the record to be updated. Required, if id will not be specified. */
    private UpdateKey updateKey;

    /**
    * Field codes and values are specified in this object. If set to null, the record will not be
    * updated.
    */
    private Record record;

    /**
    * The expected revision number (optional). If the value does not match, an error will occur and
    * the record will not be updated. If the value is null or -1, the revision number will not be
    * checked.
    */
    private Long revision;
}
