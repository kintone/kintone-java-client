package com.kintone.client.model.app;

import java.util.Map;
import lombok.Value;

/** An object consisting of permissions of the specified record. */
@Value
public class EvaluatedRecordRight {

    /** The record ID. */
    private final long id;

    /** An object consisting of record permissions of the specified record ID. */
    private final EvaluatedRecordRightEntity record;

    /** An object consisting of field permissions of the specified record ID. */
    private final Map<String, EvaluatedFieldRightEntity> fields;
}
