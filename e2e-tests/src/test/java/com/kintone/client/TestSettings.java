package com.kintone.client;

import com.kintone.client.exception.KintoneRuntimeException;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import lombok.Getter;
import org.apache.hc.client5.http.ContextBuilder;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.Timeout;

@Getter
public class TestSettings {
    private final String baseUrl;
    private final UserSetting defaultUser;
    private final UserSetting testUser;
    private final String basicAuthUser;
    private final String basicAuthPass;
    private final String clientCertPath;
    private final String clientCertPass;
    private final HttpHost proxyHost;
    private final String proxyUrl;
    private final String proxyUser;
    private final String proxyPassword;

    private final boolean localMode;

    private final Long singleThreadSpaceId;
    private final Long multiThreadSpaceId;
    private final Long multiThreadDefaultThreadId;
    private final Long guestSpaceId;
    private final Long templateId;

    private static final TestSettings INSTANCE = new TestSettings();

    public static TestSettings get() {
        return INSTANCE;
    }

    private TestSettings() {
        baseUrl = getEnv("KINTONE_BASE_URL", "http://localhost");

        String defaultUserCode = getEnv("KINTONE_DEFAULT_USER", "cybozu");
        String defaultUserPassword = getEnv("KINTONE_DEFAULT_PASSWORD", "cybozu");
        defaultUser = new UserSetting(defaultUserCode, defaultUserCode, defaultUserPassword);

        String testUserCode = getEnv("KINTONE_TEST_USER", "user1");
        String testUserPassword = getEnv("KINTONE_TEST_PASSWORD", "user1");
        testUser = new UserSetting(testUserCode, testUserCode, testUserPassword);

        basicAuthUser = getEnv("KINTONE_BASIC_USER", "");
        basicAuthPass = getEnv("KINTONE_BASIC_PASS", "");
        clientCertPath = getEnv("KINTONE_CLIENT_CERT", "");
        clientCertPass = getEnv("KINTONE_CLIENT_CERT_PASS", "");
        proxyUrl = getEnv("KINTONE_PROXY_URL", "");
        proxyUser = getEnv("KINTONE_PROXY_USER", "");
        proxyPassword = getEnv("KINTONE_PROXY_PASS", "");
        localMode = initIsLocal(baseUrl);

        singleThreadSpaceId = getLongEnv("KINTONE_SPACE_ID");
        multiThreadSpaceId = getLongEnv("KINTONE_MULTI_THREAD_SPACE_ID");
        multiThreadDefaultThreadId = getLongEnv("KINTONE_MULTI_THREAD_DEFAULT_THREAD_ID");
        guestSpaceId = getLongEnv("KINTONE_GUEST_SPACE_ID");
        templateId = getLongEnv("KINTONE_TEMPLATE_ID");

        proxyHost = Objects.equals(proxyUrl, "") ? null : createProxyHost(URI.create(proxyUrl));
    }

    private static boolean initIsLocal(String baseUrl) {
        // 環境変数 KINTONE_IS_LOCALがtrueかbaseUrlがlocalhostならローカル実行とする
        String env = System.getenv("KINTONE_IS_LOCAL");
        if (env != null && env.equals("true")) {
            return true;
        }
        return baseUrl.startsWith("http://localhost");
    }

    private static String getEnv(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    private static Long getLongEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Long.parseLong(value);
    }

    public CloseableHttpClient createHttpClient() {
        final Timeout timeout = Timeout.of(60000, TimeUnit.MILLISECONDS);

        ConnectionConfig connectionConfig =
                ConnectionConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout).build();

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        configBuilder.setConnectionRequestTimeout(timeout);
        if (proxyHost != null) {
            configBuilder.setProxyPreferredAuthSchemes(Collections.singleton("basic"));
        }

        SSLConnectionSocketFactoryBuilder sslConfigBuilder = SSLConnectionSocketFactoryBuilder.create();
        sslConfigBuilder.setTlsVersions(TLS.V_1_3, TLS.V_1_2);
        if (!clientCertPath.isEmpty()) {
            SSLContext sslContext = createSSLContext(clientCertPath, clientCertPass);
            sslConfigBuilder.setSslContext(sslContext);
        } else {
            sslConfigBuilder.setSslContext(SSLContexts.createSystemDefault());
        }

        PoolingHttpClientConnectionManager connectionManager =
                PoolingHttpClientConnectionManagerBuilder.create()
                        .setSSLSocketFactory(sslConfigBuilder.build())
                        .setDefaultConnectionConfig(connectionConfig)
                        .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
                        .setConnPoolPolicy(PoolReusePolicy.LIFO)
                        .build();

        HttpClientBuilder clientBuilder = HttpClients.custom();
        if (proxyHost != null) {
            clientBuilder.setProxy(proxyHost);

            // proxy認証情報があれば設定
            if (!proxyUser.isEmpty() && !proxyPassword.isEmpty()) {
                BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(
                        new AuthScope(proxyHost),
                        new UsernamePasswordCredentials(proxyUser, proxyPassword.toCharArray()));
                clientBuilder.setDefaultCredentialsProvider(credsProvider);
            }
        }
        clientBuilder.setDefaultRequestConfig(configBuilder.build());
        clientBuilder.setConnectionManager(connectionManager);
        clientBuilder.disableRedirectHandling();
        return clientBuilder.build();
    }

    private SSLContext createSSLContext(String path, String password) {
        char[] pass = password.toCharArray();
        try (FileInputStream stream0 = new FileInputStream(path)) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(stream0, pass);
            return SSLContexts.custom().loadKeyMaterial(keyStore, pass).build();
        } catch (IOException | GeneralSecurityException e) {
            throw new KintoneRuntimeException("Failed to create ssl context.", e);
        }
    }

    private static HttpHost createProxyHost(URI proxyHost) {
        return proxyHost == null
                ? null
                : new HttpHost(proxyHost.getScheme(), proxyHost.getHost(), proxyHost.getPort());
    }

    /**
     * Basic認証用のヘッダを積んでHTTP リクエストするための{@code HttpClientContext}を生成。<br>
     * {@code createHttpClient()}で取得した{@code CloseableHttpClient}でリクエストする際にこのコンテキストを設定すること
     *
     * @return Basic認証用のヘッダを積んでHTTP リクエストするためのコンテキスト
     */
    public HttpClientContext createHttpClientContext() {
        ContextBuilder builder = ContextBuilder.create();
        if (!basicAuthUser.isEmpty()) {
            URI uri = null;
            try {
                uri = new URI(baseUrl);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            HttpHost target = new HttpHost(uri.getScheme(), uri.getHost(), uri.getPort());
            builder.preemptiveBasicAuth(
                    target, new UsernamePasswordCredentials(basicAuthUser, basicAuthPass.toCharArray()));
        }
        return builder.build();
    }
}
