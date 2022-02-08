package com.kintone.client;

import com.kintone.client.exception.KintoneRuntimeException;
import com.kintone.client.model.Auth;
import com.kintone.client.model.BasicAuth;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.net.ssl.SSLContext;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.ssl.SSLContexts;

/**
 * A builder for {@link KintoneClient}.
 *
 * <p>First, creates an instance of the builder using {@link #create(String)} with your Kintone URL.
 *
 * <pre>{@code
 * KintoneClientBuilder builder = KintoneClientBuilder.create("https://{your Kintone domain}.cybozu.com");
 * }</pre>
 *
 * A client requires an authentication setting, sets using one of the following:
 *
 * <ul>
 *   <li>{@link #authByPassword(String, String)}
 *   <li>{@link #authByApiToken(String)}
 *   <li>{@link #authByApiToken(Collection)}
 * </ul>
 *
 * <p>Other settings, like a Guest Space ID for specifying the operating Guest Space, Basic
 * authentication settings and an additional user agent string, are optional. After completing the
 * configurations, gets a {@link KintoneClient} by calling {@link #build()}.
 *
 * <pre>{@code
 * KintoneClient client = builder.authByPassword(user, password).build();
 * }</pre>
 *
 * <p>This class also provides shorthand methods for typical setups:
 *
 * <ul>
 *   <li>{@link #defaultClient(String, String, String)} for setup with Password authentication
 *   <li>{@link #defaultClient(String, String)} for setup with API token authentication
 *   <li>{@link #defaultClient(String, List)} for setup with API token authentication using multiple
 *       tokens
 * </ul>
 */
public class KintoneClientBuilder {

    private static final int DEFAULT_TIMEOUT = 60 * 1000; // ms
    private String baseUrl;
    private Auth auth;
    private URI proxyHost;
    private SSLContext sslContext;
    private int connectionTimeout = DEFAULT_TIMEOUT;
    private int socketTimeout = DEFAULT_TIMEOUT;
    private int connectionRequestTimeout = DEFAULT_TIMEOUT;
    private BasicAuth basicAuth;
    private Long guestSpaceId;
    private String appendixUserAgent;

    private KintoneClientBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Returns an instance of this builder.
     *
     * @param baseUrl the Kintone URL
     * @return the builder
     */
    public static KintoneClientBuilder create(String baseUrl) {
        return new KintoneClientBuilder(baseUrl);
    }

    /**
     * Builds a {@link KintoneClient} with Password authentication.
     *
     * @param baseUrl the Kintone URL
     * @param user the login name of the user
     * @param password the password
     * @return the client
     */
    public static KintoneClient defaultClient(String baseUrl, String user, String password) {
        return new KintoneClientBuilder(baseUrl).authByPassword(user, password).build();
    }

    /**
     * Builds a {@link KintoneClient} with API token authentication.
     *
     * @param baseUrl the Kintone URL
     * @param apiToken the API token
     * @return the client
     */
    public static KintoneClient defaultClient(String baseUrl, String apiToken) {
        return new KintoneClientBuilder(baseUrl).authByApiToken(apiToken).build();
    }

    /**
     * Builds a {@link KintoneClient} with API token authentication using multiple API tokens.
     *
     * @param baseUrl the Kintone URL
     * @param apiTokens the list of API tokens
     * @return the client
     */
    public static KintoneClient defaultClient(String baseUrl, List<String> apiTokens) {
        return new KintoneClientBuilder(baseUrl).authByApiToken(apiTokens).build();
    }

    /**
     * Sets the user and password for BASIC authentication. This is an optional setting.
     *
     * @param user the user of BASIC authentication
     * @param password the password of BASIC authentication
     * @return this builder instance
     */
    public KintoneClientBuilder withBasicAuth(String user, String password) {
        this.basicAuth = new BasicAuth(user, password);
        return this;
    }

    /**
     * Sets Password authentication.
     *
     * @param user the login name of the user
     * @param password the password
     * @return this builder instance
     */
    public KintoneClientBuilder authByPassword(String user, String password) {
        this.auth = Auth.byPassword(user, password);
        return this;
    }

    /**
     * Sets API token authentication.
     *
     * @param apiToken the API token
     * @return this builder instance
     */
    public KintoneClientBuilder authByApiToken(String apiToken) {
        this.auth = Auth.byApiToken(apiToken);
        return this;
    }

    /**
     * Sets API token authentication.
     *
     * @param apiTokens the list of API tokens
     * @return this builder instance
     */
    public KintoneClientBuilder authByApiToken(Collection<String> apiTokens) {
        this.auth = Auth.byApiToken(new ArrayList<>(apiTokens));
        return this;
    }

    /**
     * Sets the proxy settings. This is an optional setting.
     *
     * @param scheme the proxy scheme
     * @param hostname the proxy server name
     * @param port the proxy port
     * @return this builder instance
     */
    public KintoneClientBuilder withProxy(String scheme, String hostname, int port) {
        try {
            this.proxyHost = new URIBuilder().setScheme(scheme).setHost(hostname).setPort(port).build();
        } catch (URISyntaxException e) {
            throw new KintoneRuntimeException("Failed to create uri object.", e);
        }
        return this;
    }

    /**
     * Sets the certificate for client certificate authentication. This is an optional setting.
     *
     * @param certificate the path of client certificate file
     * @param password the password for the certificate
     * @return this builder instance
     */
    public KintoneClientBuilder withClientCertificate(Path certificate, String password) {
        try {
            return withClientCertificate(Files.newInputStream(certificate), password);
        } catch (IOException e) {
            throw new KintoneRuntimeException("Failed to create ssl context.", e);
        }
    }

    /**
     * Sets the certificate for client certificate authentication. This is an optional setting.
     *
     * @param stream the input stream of client certificate data
     * @param password the password for the certificate
     * @return this builder instance
     */
    public KintoneClientBuilder withClientCertificate(InputStream stream, String password) {
        this.sslContext = createSSLContext(stream, password);
        return this;
    }

    private SSLContext createSSLContext(InputStream stream, String password) {
        char[] pass = password.toCharArray();
        try (InputStream stream0 = stream) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(stream0, pass);
            return SSLContexts.custom().loadKeyMaterial(keyStore, pass).build();
        } catch (IOException | GeneralSecurityException e) {
            throw new KintoneRuntimeException("Failed to create ssl context.", e);
        }
    }

    /**
     * Sets the Guest Space ID for specifying the operating Guest Space. To operate with a Guest Space
     * or Apps in a Guest Space, this setting is required. Clients must be created for each Guest
     * Space.
     *
     * @param guestSpaceId the Guest Space ID
     * @return this builder instance
     */
    public KintoneClientBuilder setGuestSpaceId(long guestSpaceId) {
        this.guestSpaceId = guestSpaceId;
        return this;
    }

    /**
     * Sets the additional user agent string. This is an optional setting.
     *
     * @param appendixUserAgent the additional user agent string
     * @return this builder instance
     */
    public KintoneClientBuilder setAppendixUserAgent(String appendixUserAgent) {
        this.appendixUserAgent = appendixUserAgent;
        return this;
    }

    /**
     * Sets the connection timeout in milliseconds.
     *
     * @param connectionTimeout the timeout in milliseconds
     * @return this builder instance
     */
    public KintoneClientBuilder setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    /**
     * Sets the socket timeout in milliseconds.
     *
     * @param socketTimeout the timeout in milliseconds
     * @return this builder instance
     */
    public KintoneClientBuilder setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    /**
     * Sets the connection request timeout in milliseconds.
     *
     * @param connectionRequestTimeout the timeout in milliseconds
     * @return this builder instance
     */
    public KintoneClientBuilder setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
        return this;
    }

    public KintoneClient build() {
        InternalClientImpl client =
                new InternalClientImpl(
                        baseUrl,
                        auth,
                        basicAuth,
                        proxyHost,
                        sslContext,
                        guestSpaceId,
                        appendixUserAgent,
                        connectionTimeout,
                        socketTimeout,
                        connectionRequestTimeout);
        return new KintoneClient(client);
    }
}
