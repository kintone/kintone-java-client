package com.kintone.client.model.record;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Value;

/** A value object for a Multi-choice field. */
@Value
public class MultiSelectFieldValue implements FieldValue {

    /** The list of selected options of the Multi-choice. */
    private final List<String> values;

    public MultiSelectFieldValue(List<String> values) {
        this.values = Collections.unmodifiableList(values);
    }

    public MultiSelectFieldValue(String... values) {
        this(Arrays.asList(values));
    }

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.MULTI_SELECT;
    }
}
