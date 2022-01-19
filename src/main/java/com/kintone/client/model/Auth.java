package com.kintone.client.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public interface Auth {

    /**
     * Returns HTTP headers of each authentication.
     *
     * @return a map. The keys are HTTP header names, and the values are header values.
     */
    Map<String, String> getHeaders();

    /**
     * Creates an {@link Auth} instance for Password authentication.
     *
     * @param username the user name
     * @param password the password
     * @return an object for Password authentication
     */
    static Auth byPassword(String username, String password) {
        return new PasswordAuth(username, password);
    }

    /**
     * Creates an {@link Auth} instance for API token authentication.
     *
     * @param apiToken an API token
     * @return an object for API token authentication
     */
    static Auth byApiToken(String apiToken) {
        return new ApiTokenAuth(Collections.singletonList(apiToken));
    }

    /**
     * Creates an {@link Auth} instance for API token authentication with multiple API tokens.
     *
     * @param apiTokens a list of API tokens
     * @return an object for API token authentication
     */
    static Auth byApiToken(Collection<String> apiTokens) {
        return new ApiTokenAuth(apiTokens);
    }
}
