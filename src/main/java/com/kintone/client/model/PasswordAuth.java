package com.kintone.client.model;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class PasswordAuth implements Auth {
    private static final String HEADER_NAME = "X-Cybozu-Authorization";
    private final String username;
    private final String password;

    @Override
    public Map<String, String> getHeaders() {
        String body = username + ":" + password;
        String headerValue = Base64.getEncoder().encodeToString(body.getBytes());
        Map<String, String> headers = new HashMap<>();
        headers.put(HEADER_NAME, headerValue);
        return headers;
    }
}
