package com.kintone.client.model.record;

import com.kintone.client.model.Organization;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Value;

/** A value object for a Department Selection field. */
@Value
public class OrganizationSelectFieldValue implements FieldValue {

    /** The list of selected departments. */
    private final List<Organization> values;

    public OrganizationSelectFieldValue(List<Organization> values) {
        this.values = Collections.unmodifiableList(values);
    }

    public OrganizationSelectFieldValue(Organization... values) {
        this(Arrays.asList(values));
    }

    /** {@inheritDoc} */
    @Override
    public FieldType getType() {
        return FieldType.ORGANIZATION_SELECT;
    }
}
