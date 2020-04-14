package com.kintone.client.model.record;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Value;

/** A value object of a Checkbox field. */
@Value
public class CheckBoxFieldValue implements FieldValue {

    /** The list of selected options of the Checkbox. */
    private final List<String> values;

    public CheckBoxFieldValue(List<String> values) {
        this.values = Collections.unmodifiableList(values);
    }

    public CheckBoxFieldValue(String... values) {
        this(Arrays.asList(values));
    }

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.CHECK_BOX;
    }
}
