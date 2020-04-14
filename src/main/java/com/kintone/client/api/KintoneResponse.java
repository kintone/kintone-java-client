package com.kintone.client.api;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** An object containing the response data returned by Kintone APIs. */
@Getter
@RequiredArgsConstructor
public class KintoneResponse<T extends KintoneResponseBody> {

    /** The HTTP status code. */
    private final int statusCode;

    /** The HTTP response headers. */
    private final Map<String, Object> headers;

    /** The content of response. */
    private final T body;

    /** The error content of response as String. */
    private final String errorBody;
}
