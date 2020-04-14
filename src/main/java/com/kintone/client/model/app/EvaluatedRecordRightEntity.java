package com.kintone.client.model.app;

import lombok.Value;

/** An object consisting of record permissions of the specified record. */
@Value
public class EvaluatedRecordRightEntity {

    /** The view permissions of the specified record ID. */
    private final boolean viewable;

    /** The edit permissions of the specified record ID. */
    private final boolean editable;

    /** The delete permissions of the specified record ID. */
    private final boolean deletable;
}
