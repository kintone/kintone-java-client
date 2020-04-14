package com.kintone.client.model.schema;

import lombok.Value;

/** An object containing information of an API. */
@Value
public class ApiSchemaLink {

    /** The URL of the API, that can be used with the Get Schema API. */
    private final String link;
}
