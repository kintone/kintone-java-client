package com.kintone.client.api.schema;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.schema.ApiSchemaLink;
import java.util.Map;
import lombok.Value;

/** A response object for Get API List API. */
@Value
public class GetApiListResponseBody implements KintoneResponseBody {

    /** The base URL that will be used with Get API Schema API. */
    private final String baseUrl;

    /** An object containing information of each API. The key represents the ID string of each API */
    private final Map<String, ApiSchemaLink> apis;
}
