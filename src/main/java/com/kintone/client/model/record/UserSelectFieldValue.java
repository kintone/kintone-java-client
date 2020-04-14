package com.kintone.client.model.record;

import com.kintone.client.model.User;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Value;

/** A value object for an User Selection field. */
@Value
public class UserSelectFieldValue implements FieldValue {

    /** The list of selected users. */
    private List<User> values;

    public UserSelectFieldValue(List<User> values) {
        this.values = Collections.unmodifiableList(values);
    }

    public UserSelectFieldValue(User... values) {
        this(Arrays.asList(values));
    }

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.USER_SELECT;
    }
}
