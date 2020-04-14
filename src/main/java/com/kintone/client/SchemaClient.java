package com.kintone.client;

import com.kintone.client.api.schema.GetApiListResponseBody;
import com.kintone.client.api.schema.GetApiSchemaResponseBody;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/** A client that operates schema APIs. */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SchemaClient {
    private final InternalClient client;
    private final List<ResponseHandler> handlers;

    /**
    * Gets the list of App/Record/Space APIs available to use on kintone.
    *
    * @return the response data. See {@link GetApiListResponseBody}
    */
    public GetApiListResponseBody getApiList() {
        return client.call(KintoneApi.GET_API_LIST, null, handlers);
    }

    /**
    * Gets the API schema info of a kintone API.
    *
    * @param api the target api
    * @return the response data. See {@link GetApiSchemaResponseBody}
    */
    public GetApiSchemaResponseBody getApiSchema(KintoneApi api) {
        if (api == KintoneApi.GET_API_LIST) {
            return null;
        }
        String path = "/k/v1/" + toApiPath(api);
        return client.call(KintoneHttpMethod.GET, path, null, GetApiSchemaResponseBody.class, handlers);
    }

    /**
    * Gets the API schema info of a kintone API.
    *
    * @param link the path of target api
    * @return the response data. See {@link GetApiSchemaResponseBody}
    */
    public GetApiSchemaResponseBody getApiSchema(String link) {
        boolean valid =
                Arrays.stream(KintoneApi.values())
                        .anyMatch(api -> api != KintoneApi.GET_API_LIST && toApiPath(api).equals(link));
        if (!valid) {
            return null;
        }
        String path = "/k/v1/" + link;
        return client.call(KintoneHttpMethod.GET, path, null, GetApiSchemaResponseBody.class, handlers);
    }

    private String toApiPath(KintoneApi api) {
        return "apis/" + api.getEndpoint() + "/" + api.getMethod().toString().toLowerCase() + ".json";
    }
}
