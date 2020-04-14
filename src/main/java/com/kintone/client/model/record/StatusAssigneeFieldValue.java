package com.kintone.client.model.record;

import com.kintone.client.model.User;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Value;

/** A value object for a Assignee field. */
@Value
public class StatusAssigneeFieldValue implements FieldValue {

    /** The value of the Assignee field. */
    private final List<User> values;

    public StatusAssigneeFieldValue(List<User> values) {
        this.values = Collections.unmodifiableList(values);
    }

    public StatusAssigneeFieldValue(User... values) {
        this(Arrays.asList(values));
    }

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.STATUS_ASSIGNEE;
    }
}
