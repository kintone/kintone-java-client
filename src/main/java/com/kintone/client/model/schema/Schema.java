package com.kintone.client.model.schema;

import java.util.List;
import java.util.Map;
import lombok.Value;

/** The schema information common within all APIs, which retrieved by Get API Schema API. */
@Value
public class Schema {

    /** The properties of this schema. */
    private final Map<String, Object> properties;

    /** The type of this schema object. */
    private final String type;

    /** The list of required properties. */
    private final List<String> required;
}
