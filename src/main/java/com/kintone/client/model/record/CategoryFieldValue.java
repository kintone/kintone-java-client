package com.kintone.client.model.record;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Value;

/** A value object for a Category field. */
@Value
public class CategoryFieldValue implements FieldValue {

    /** The list of selected category labels. */
    private final List<String> values;

    public CategoryFieldValue(List<String> values) {
        this.values = Collections.unmodifiableList(values);
    }

    public CategoryFieldValue(String... values) {
        this(Arrays.asList(values));
    }

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.CATEGORY;
    }
}
