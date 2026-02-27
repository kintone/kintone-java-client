package com.kintone.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class ApiTestBase {

    private static final int TIMEOUT_MS = 60000;

    private final List<KintoneClient> clients = new ArrayList<>();

    private CloseableHttpClient httpClient;

    @BeforeEach
    public void setup() {
        httpClient = null;
    }

    @AfterEach
    public void cleanup() throws Exception {
        for (KintoneClient client : clients) {
            client.close();
        }
        clients.clear();

        if (httpClient != null) {
            httpClient.close();
            httpClient = null;
        }
    }

    public KintoneClient setupDefaultClient() {
        return setupDefaultClient(null);
    }

    public KintoneClient setupDefaultClient(Long guestSpaceId) {
        KintoneClientBuilder builder =
                KintoneClientBuilder.create(getBaseURL())
                        .authByPassword(getDefaultUser(), getDefaultUserPassword())
                        .setConnectionRequestTimeout(TIMEOUT_MS)
                        .setConnectionTimeout(TIMEOUT_MS)
                        .setSocketTimeout(TIMEOUT_MS);
        if (guestSpaceId != null) {
            builder.setGuestSpaceId(guestSpaceId);
        }

        final TestSettings settings = getSettings();
        if (!settings.getBasicAuthUser().isEmpty()) {
            builder.withBasicAuth(settings.getBasicAuthUser(), settings.getBasicAuthPass());
        }
        if (!settings.getClientCertPath().isEmpty()) {
            Path path = FileSystems.getDefault().getPath(settings.getClientCertPath());
            builder.withClientCertificate(path, settings.getClientCertPass());
        }
        getProxyURL()
                .ifPresent(
                        proxy -> {
                            builder.withProxy(proxy.getScheme(), proxy.getHost(), proxy.getPort());
                            if (!settings.getProxyUser().isEmpty()) {
                                builder.setProxyAuthentication(
                                        settings.getProxyUser(), settings.getProxyPassword());
                            }
                        });
        KintoneClient client = builder.build();
        clients.add(client);
        return client;
    }

    public CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = getSettings().createHttpClient();
        }
        return httpClient;
    }

    public TestSettings getSettings() {
        return TestSettings.get();
    }

    public Long getDefaultUserId() {
        return getSettings().getDefaultUserId();
    }

    public String getBaseURL() {
        return getSettings().getBaseUrl();
    }

    public Optional<URI> getProxyURL() {
        String proxyUrl = getSettings().getProxyUrl();
        if (proxyUrl == null || proxyUrl.isEmpty()) {
            return Optional.empty();
        }
        try {
            URI uri = new URI(proxyUrl);
            return Optional.of(uri);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String getDefaultUser() {
        return getSettings().getDefaultUser().getCode();
    }

    public String getDefaultUserPassword() {
        return getSettings().getDefaultUser().getPassword();
    }
}
