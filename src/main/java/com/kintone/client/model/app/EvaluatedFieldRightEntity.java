package com.kintone.client.model.app;

import lombok.Value;

/** An object consisting of field permissions of the specified record. */
@Value
public class EvaluatedFieldRightEntity {

    /**
     * The view permissions of the field of the specified record ID. If the user has no view
     * permissions of the record, all the values are set as false.
     */
    private final boolean viewable;

    /** The edit permissions of the field of the specified record ID. */
    private final boolean editable;
}
