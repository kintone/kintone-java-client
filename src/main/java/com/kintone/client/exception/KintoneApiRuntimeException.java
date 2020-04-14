package com.kintone.client.exception;

import java.util.Collections;
import java.util.Map;
import lombok.Getter;

/** An exception when Kintone returns a response other than HTTP 200 status. */
@Getter
public class KintoneApiRuntimeException extends KintoneRuntimeException {
    private static final long serialVersionUID = 3766950253072962607L;

    /** The HTTP status code. */
    private final int statusCode;

    /** The HTTP response headers. */
    private final Map<String, Object> headers;

    /** HTTP response content or {@code null} for none. */
    private final String content;

    public KintoneApiRuntimeException(int statusCode, Map<String, Object> headers, String content) {
        super("HTTP error status " + statusCode + ", " + content);
        this.statusCode = statusCode;
        this.headers = Collections.unmodifiableMap(headers);
        this.content = content;
    }
}
