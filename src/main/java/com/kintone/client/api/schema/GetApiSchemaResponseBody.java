package com.kintone.client.api.schema;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.schema.RequestSchema;
import com.kintone.client.model.schema.ResponseSchema;
import com.kintone.client.model.schema.Schema;
import java.util.Map;
import lombok.Value;

/** A response object for Get API Schema API. */
@Value
public class GetApiSchemaResponseBody implements KintoneResponseBody {

    /** The ID of the API. */
    private final String id;

    /** The base URL starting with "https://" that will be used with the API. */
    private final String baseUrl;

    /** The API path, such as "records.json". */
    private final String path;

    /** The HTTP method for the API */
    private final String httpMethod;

    /** The schema information for the API request, in a JSON Schema format. */
    private final RequestSchema request;

    /** The schema information for the API response, in a JSON Schema format. */
    private final ResponseSchema response;

    /** The schema information common within all APIs. */
    private final Map<String, Schema> schemas;
}
