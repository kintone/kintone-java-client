package com.kintone.client.model.record;

import com.kintone.client.model.Group;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Value;

/** A value object for a Group Selection field. */
@Value
public class GroupSelectFieldValue implements FieldValue {

    /** The list of selected groups. */
    private final List<Group> values;

    public GroupSelectFieldValue(List<Group> values) {
        this.values = Collections.unmodifiableList(values);
    }

    public GroupSelectFieldValue(Group... values) {
        this(Arrays.asList(values));
    }

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.GROUP_SELECT;
    }
}
