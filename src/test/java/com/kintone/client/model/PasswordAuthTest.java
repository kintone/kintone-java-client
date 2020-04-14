package com.kintone.client.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

public class PasswordAuthTest {

    @Test
    public void getHeaders() {
        PasswordAuth sut = new PasswordAuth("user1", "pass1");
        Map<String, String> headers = sut.getHeaders();
        assertThat(headers).hasSize(1);
        assertThat(headers).containsEntry("X-Cybozu-Authorization", "dXNlcjE6cGFzczE=");
    }
}
