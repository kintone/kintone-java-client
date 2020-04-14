package com.kintone.client.model.schema;

import java.util.List;
import java.util.Map;
import lombok.Value;

/** A schema information for an API response, which retrieved by Get API Schema API. */
@Value
public class ResponseSchema {

    /** The properties of this schema. */
    private final Map<String, Object> properties;

    /** The type of this schema object. */
    private final String type;

    /** The list of required properties. */
    private final List<String> required;
}
