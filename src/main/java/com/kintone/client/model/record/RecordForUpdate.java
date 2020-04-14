package com.kintone.client.model.record;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/** An object that stores information for updating a record. */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecordForUpdate {

    /** The Record ID of the record to be updated. Either "id" or "updateKey" is required. */
    private final Long id;

    /** The unique key of the record to be updated. Required, if id will not be specified. */
    private final UpdateKey updateKey;

    /**
    * Field codes and values are specified in this object. If set to null, the record will not be
    * updated.
    */
    private final Record record;

    /** The expected revision number (optional). */
    private final Long revision;

    public RecordForUpdate(long id, Record record) {
        this(id, null, record, null);
    }

    public RecordForUpdate(long id, Record record, long revision) {
        this(id, null, record, revision);
    }

    public RecordForUpdate(UpdateKey key, Record record) {
        this(null, key, record, null);
    }

    public RecordForUpdate(UpdateKey key, Record record, long revision) {
        this(null, key, record, revision);
    }
}
