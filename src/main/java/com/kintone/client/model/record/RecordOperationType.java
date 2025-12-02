package com.kintone.client.model.record;

/** The type of operation performed on a record when using UPSERT mode in Update Records API. */
public enum RecordOperationType {
    /** An existing record was updated. */
    UPDATE,

    /** A new record was created. */
    INSERT
}
