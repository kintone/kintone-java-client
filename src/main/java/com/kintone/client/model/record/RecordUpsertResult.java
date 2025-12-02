package com.kintone.client.model.record;

import lombok.Value;

/** A value that contains the record ID, revision, and operation, returned by Upsert Records API. */
@Value
public class RecordUpsertResult {

    /** The Record ID */
    private final long id;

    /** The revision number of the record after updating or creating the record. */
    private final long revision;

    /**
     * The operation that was performed. UPDATE (existing record was updated) or INSERT (new record
     * was created). Null when not using UPSERT mode.
     */
    private final RecordOperationType operation;
}
