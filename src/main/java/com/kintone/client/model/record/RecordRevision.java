package com.kintone.client.model.record;

import lombok.Value;

/**
 * A value that contains the record ID and the revision, returned by Update Records API and Update
 * Record Statuses API.
 */
@Value
public class RecordRevision {

    /** The Record ID */
    private final long id;

    /** The revision number of the record after updating the record. */
    private final long revision;
}
