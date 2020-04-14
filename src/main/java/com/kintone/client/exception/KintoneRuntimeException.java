package com.kintone.client.exception;

/** An exception when an unexpected error occurs inside Kintone client library. */
public class KintoneRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -8842468005496195186L;

    public KintoneRuntimeException() {
        super();
    }

    public KintoneRuntimeException(String message) {
        super(message);
    }

    public KintoneRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
