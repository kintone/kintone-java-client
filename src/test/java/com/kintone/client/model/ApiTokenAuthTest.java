package com.kintone.client.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ApiTokenAuthTest {

    @Test
    public void getHeaders_singleToken() {
        ApiTokenAuth sut = new ApiTokenAuth(Collections.singletonList("single_token"));
        Map<String, String> headers = sut.getHeaders();
        assertThat(headers).hasSize(1);
        assertThat(headers).containsEntry("X-Cybozu-API-Token", "single_token");
    }

    @Test
    public void getHeaders_multipleToken() {
        ApiTokenAuth sut = new ApiTokenAuth(Arrays.asList("token1", "token2", "token3"));
        Map<String, String> headers = sut.getHeaders();
        assertThat(headers).hasSize(1);
        assertThat(headers).containsEntry("X-Cybozu-API-Token", "token1,token2,token3");
    }
}
