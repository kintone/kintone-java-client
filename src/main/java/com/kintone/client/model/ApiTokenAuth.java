package com.kintone.client.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ApiTokenAuth implements Auth {
    private final Collection<String> apiTokens;

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        String value = String.join(",", apiTokens);
        headers.put("X-Cybozu-API-Token", value);
        return headers;
    }
}
