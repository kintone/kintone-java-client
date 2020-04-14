package com.kintone.client.model.record;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Value;

/** A value object for a Table. */
@Value
public class SubtableFieldValue implements FieldValue {

    /** The rows of the Table. */
    private final List<TableRow> rows;

    public SubtableFieldValue(List<TableRow> rows) {
        this.rows = Collections.unmodifiableList(rows);
    }

    public SubtableFieldValue(TableRow... rows) {
        this(Arrays.asList(rows));
    }

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.SUBTABLE;
    }
}
