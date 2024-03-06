package com.kintone.client;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.api.KintoneResponse;
import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.api.common.BulkRequestsRequest;
import com.kintone.client.api.common.BulkRequestsResponseBody;
import com.kintone.client.api.common.DownloadFileRequest;
import com.kintone.client.api.common.DownloadFileResponseBody;
import com.kintone.client.api.common.UploadFileResponseBody;
import com.kintone.client.exception.KintoneRuntimeException;
import com.kintone.client.model.Auth;
import com.kintone.client.model.BasicAuth;
import com.kintone.client.model.BulkRequestContent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import javax.net.ssl.SSLContext;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.InputStreamBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.auth.BasicScheme;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class InternalClientImpl extends InternalClient {

    private final CloseableHttpClient httpClient;
    private final String baseUrl;
    private final Auth auth;
    private final BasicAuth basicAuth;
    private final Long guestId;
    private final String userAgent;
    private final JsonMapper mapper;
    private final HttpHost proxyHost;
    private final BasicScheme proxyAuth;

    private static final String REQUEST_LOGGER_NAME = "com.kintone.client.requestLog";
    private final Logger logger = LoggerFactory.getLogger(REQUEST_LOGGER_NAME);

    InternalClientImpl(
            String baseUrl,
            Auth auth,
            BasicAuth basicAuth,
            URI proxyHost,
            BasicAuth proxyAuth,
            SSLContext sslContext,
            Long guestId,
            String appendixUserAgent,
            int connectionTimeout,
            int socketTimeout,
            int connectionRequestTimeout) {
        this.baseUrl = baseUrl;
        this.auth = auth;
        this.basicAuth = basicAuth;
        this.guestId = guestId;
        this.userAgent = getUserAgent(appendixUserAgent);
        this.mapper = new JsonMapper();
        this.proxyHost = createProxyHost(proxyHost);
        this.proxyAuth = createPreemptiveProxyAuth(proxyAuth);
        this.httpClient =
                createHttpClient(
                        sslContext, this.proxyHost, connectionTimeout, socketTimeout, connectionRequestTimeout);
    }

    private static CloseableHttpClient createHttpClient(
            SSLContext sslContext,
            HttpHost proxyHost,
            int connTimeout,
            int socketTimeout,
            int connRequestTimeout) {
        ConnectionConfig connectionConfig =
                ConnectionConfig.custom()
                        .setConnectTimeout(connTimeout, TimeUnit.MILLISECONDS)
                        .setSocketTimeout(socketTimeout, TimeUnit.MILLISECONDS)
                        .build();

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        configBuilder.setConnectionRequestTimeout(connRequestTimeout, TimeUnit.MILLISECONDS);
        if (proxyHost != null) {
            configBuilder.setProxyPreferredAuthSchemes(Collections.singleton("basic"));
        }

        PoolingHttpClientConnectionManager connectionManager =
                PoolingHttpClientConnectionManagerBuilder.create()
                        .setSSLSocketFactory(
                                SSLConnectionSocketFactoryBuilder.create()
                                        .setSslContext(sslContext)
                                        .setTlsVersions(TLS.V_1_3, TLS.V_1_2)
                                        .build())
                        .setDefaultConnectionConfig(connectionConfig)
                        .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
                        .setConnPoolPolicy(PoolReusePolicy.LIFO)
                        .build();

        HttpClientBuilder clientBuilder = HttpClients.custom();
        if (proxyHost != null) {
            clientBuilder.setProxy(proxyHost);
        }
        clientBuilder.setDefaultRequestConfig(configBuilder.build());
        clientBuilder.setConnectionManager(connectionManager);
        clientBuilder.disableRedirectHandling();
        return clientBuilder.build();
    }

    private static HttpHost createProxyHost(URI proxyHost) {
        if (proxyHost == null) {
            return null;
        }
        return new HttpHost(proxyHost.getScheme(), proxyHost.getHost(), proxyHost.getPort());
    }

    private static BasicScheme createPreemptiveProxyAuth(BasicAuth proxyAuth) {
        if (proxyAuth == null) {
            return null;
        }
        BasicScheme basicScheme = new BasicScheme();
        basicScheme.initPreemptive(
                new UsernamePasswordCredentials(
                        proxyAuth.getUser(), proxyAuth.getPassword().toCharArray()));
        return basicScheme;
    }

    private HttpContext createHttpContext() {
        HttpClientContext context = HttpClientContext.create();
        if (proxyHost != null && proxyAuth != null) {
            context.resetAuthExchange(proxyHost, proxyAuth);
        }
        return context;
    }

    private String readInputStream(InputStream in) {
        try (InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[1024];
            int size;
            while ((size = reader.read(buffer)) > 0) {
                sb.append(buffer, 0, size);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    <T extends KintoneResponseBody> T call(
            KintoneApi api, KintoneRequest body, List<ResponseHandler> handlers) {
        String path = getApiPath(api);
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) api.getResponseClass();
        return call(api.getMethod(), path, body, clazz, handlers);
    }

    @Override
    <T extends KintoneResponseBody> T call(
            KintoneHttpMethod method,
            String path,
            KintoneRequest body,
            Class<T> clazz,
            List<ResponseHandler> handlers) {
        if (logger.isDebugEnabled()) {
            String json = mapper.formatString(body);
            logger.debug("request: {} {} {}", method, path, json);
        }

        HttpUriRequest request = createJsonRequest(method, path, body);
        try {
            return httpClient.execute(
                    request,
                    createHttpContext(),
                    response -> {
                        KintoneResponse<T> result = parseJsonResponse(response, clazz);
                        applyHandlers(result, handlers);
                        return result.getBody();
                    });
        } catch (IOException e) {
            throw new KintoneRuntimeException("Failed to request", e);
        }
    }

    private Map<String, Object> headersToMap(Header[] headers) {
        Map<String, String> result = new HashMap<>();
        for (Header header : headers) {
            result.put(header.getName(), header.getValue());
        }
        return Collections.unmodifiableMap(result);
    }

    private Map<String, Object> createBulkRequestBody(BulkRequestsRequest req) {
        List<Map<String, Object>> requestBodies = new ArrayList<>();
        for (BulkRequestContent content : req.getRequests()) {
            Map<String, Object> r = new HashMap<>();
            r.put("api", getApiPath(content.getApi()));
            r.put("method", content.getApi().getMethod());
            r.put("payload", content.getPayload());
            requestBodies.add(r);
        }
        return Collections.singletonMap("requests", requestBodies);
    }

    private List<KintoneResponseBody> parseBulkRequestResponse(
            List<BulkRequestContent> requests, List<Object> responses) {
        List<KintoneResponseBody> resultBodies = new ArrayList<>();
        for (int i = 0; i < responses.size(); i++) {
            Class<? extends KintoneResponseBody> responseClass =
                    requests.get(i).getApi().getResponseClass();
            KintoneResponseBody body = mapper.convert(responses.get(i), responseClass);
            resultBodies.add(body);
        }
        return resultBodies;
    }

    @SuppressWarnings("unchecked")
    @Override
    BulkRequestsResponseBody bulkRequest(BulkRequestsRequest body, List<ResponseHandler> handlers) {
        KintoneHttpMethod method = KintoneApi.BULK_REQUESTS.getMethod();
        String path = getApiPath(KintoneApi.BULK_REQUESTS);
        Map<String, Object> bulkRequestBody = createBulkRequestBody(body);

        if (logger.isDebugEnabled()) {
            String json = mapper.formatString(bulkRequestBody);
            logger.debug("request: {} {} {}", method, path, json);
        }

        HttpUriRequest request = createJsonRequest(method, path, bulkRequestBody);
        try {
            return httpClient.execute(
                    request,
                    createHttpContext(),
                    response -> {
                        KintoneResponse<BulkRequestsResponseBody> resp =
                                parseResponse(
                                        response,
                                        stream -> {
                                            Map<String, Object> responseMap;
                                            if (logger.isDebugEnabled()) {
                                                String responseBody = readInputStream(stream);
                                                logger.debug(
                                                        "response status: {}, response body: {}",
                                                        response.getCode(),
                                                        responseBody);
                                                responseMap = mapper.parseString(responseBody, Map.class);
                                            } else {
                                                responseMap = mapper.parse(stream, Map.class);
                                            }
                                            List<Object> results = (List<Object>) responseMap.get("results");
                                            List<KintoneResponseBody> bodies =
                                                    parseBulkRequestResponse(body.getRequests(), results);
                                            return new BulkRequestsResponseBody(Collections.unmodifiableList(bodies));
                                        });
                        applyHandlers(resp, handlers);
                        return resp.getBody();
                    });
        } catch (IOException e) {
            throw new KintoneRuntimeException("Failed to request", e);
        }
    }

    @Override
    DownloadFileResponseBody download(DownloadFileRequest request, List<ResponseHandler> handlers) {
        KintoneHttpMethod method = KintoneApi.DOWNLOAD_FILE.getMethod();
        String path = getApiPath(KintoneApi.DOWNLOAD_FILE);

        if (logger.isDebugEnabled()) {
            String json = mapper.formatString(request);
            logger.debug("request: {} {} {}", method, path, json);
        }

        HttpUriRequest req = createJsonRequest(method, path, request);
        KintoneResponse<DownloadFileResponseBody> r;
        try {
            ClassicHttpResponse response = httpClient.executeOpen(null, req, createHttpContext());
            logger.debug("response status: {}", response.getCode());
            com.kintone.client.model.HttpResponse resp = new HttpResponseImpl(response);
            r = parseResponse(response, stream -> new DownloadFileResponseBody(resp));
        } catch (IOException e) {
            throw new KintoneRuntimeException("Failed to request", e);
        }

        applyHandlers(r, handlers);
        return r.getBody();
    }

    @Override
    KintoneResponse<UploadFileResponseBody> upload(
            String filename, String contentType, InputStream content, List<ResponseHandler> handlers) {
        String boundary = "__END_OF_PART__";

        Validator.checkContentType(contentType);
        Validator.checkFilename(filename);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(StandardCharsets.UTF_8);
        builder.setBoundary(boundary);
        builder.setMode(HttpMultipartMode.LEGACY);
        builder.setContentType(ContentType.MULTIPART_FORM_DATA);

        builder.addPart(
                "file", new InputStreamBody(content, ContentType.create(contentType), filename));

        String headerContentType = "multipart/form-data; boundary=" + boundary;
        KintoneHttpMethod method = KintoneApi.UPLOAD_FILE.getMethod();
        String path = getApiPath(KintoneApi.UPLOAD_FILE);

        logger.debug("request: {} {}, file: {}", method, path, filename);

        HttpUriRequest httpRequest = createRequest(method, path, headerContentType, builder.build());
        try {
            return httpClient.execute(
                    httpRequest,
                    createHttpContext(),
                    response -> {
                        KintoneResponse<UploadFileResponseBody> r =
                                parseJsonResponse(response, UploadFileResponseBody.class);
                        applyHandlers(r, handlers);
                        return r;
                    });
        } catch (IOException e) {
            throw new KintoneRuntimeException("Failed to request", e);
        }
    }

    private static boolean isSuccess(int statusCode) {
        return HttpStatus.SC_OK <= statusCode && statusCode < HttpStatus.SC_MULTIPLE_CHOICES;
    }

    private <T extends KintoneResponseBody> KintoneResponse<T> parseResponse(
            ClassicHttpResponse response, Function<InputStream, T> converter) {
        int statusCode = response.getCode();
        Map<String, Object> headers = headersToMap(response.getHeaders());
        T result = null;
        String errorBody = null;
        try {
            if (isSuccess(statusCode)) {
                result = converter.apply(response.getEntity().getContent());
            } else {
                errorBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                logger.debug("response status: {}, response body: {}", statusCode, errorBody);
            }
        } catch (IOException | ParseException e) {
            throw new KintoneRuntimeException("Failed to request", e);
        }
        return new KintoneResponse<>(statusCode, headers, result, errorBody);
    }

    private <T extends KintoneResponseBody> KintoneResponse<T> parseJsonResponse(
            ClassicHttpResponse response, Class<T> responseClass) {
        return parseResponse(
                response,
                stream -> {
                    if (logger.isDebugEnabled()) {
                        String body = readInputStream(stream);
                        logger.debug("response status: {}, response body: {}", response.getCode(), body);
                        return mapper.parseString(body, responseClass);
                    } else {
                        return mapper.parse(stream, responseClass);
                    }
                });
    }

    private void applyHandlers(KintoneResponse<?> response, List<ResponseHandler> handlers) {
        for (ResponseHandler handler : handlers) {
            handler.handle(response);
        }
    }

    private String getApiPath(KintoneApi api) {
        if (guestId != null) {
            return "/k/guest/" + guestId + "/v1/" + api.getEndpoint() + ".json";
        } else {
            return "/k/v1/" + api.getEndpoint() + ".json";
        }
    }

    private HttpUriRequest createRequest(
            KintoneHttpMethod method, String path, String contentType, HttpEntity entity) {
        URI uri = URI.create(this.baseUrl + path);
        HttpUriRequestBase request = new HttpUriRequestBase(method.toString(), uri);

        for (Map.Entry<String, String> header : auth.getHeaders().entrySet()) {
            request.addHeader(header.getKey(), header.getValue());
        }

        if (basicAuth != null) {
            String value = basicAuth.getUser() + ":" + basicAuth.getPassword();
            String token = Base64.getEncoder().encodeToString(value.getBytes());
            request.addHeader("Authorization", "Basic " + token);
        }

        request.addHeader("User-Agent", userAgent);
        if (entity != null) {
            request.addHeader("Content-Type", contentType);
        }

        request.setEntity(entity);
        return request;
    }

    private HttpUriRequest createJsonRequest(KintoneHttpMethod method, String path, Object body) {
        if (body == null) {
            return createRequest(method, path, null, null);
        } else {
            HttpEntity entity = new ByteArrayEntity(mapper.format(body), ContentType.APPLICATION_JSON);
            return createRequest(method, path, ContentType.APPLICATION_JSON.getMimeType(), entity);
        }
    }

    private static String getUserAgent(String appendixUserAgent) {
        String userAgent = "kintone-Java-Client@" + ConfigProperties.getVersion();
        if (appendixUserAgent != null && !appendixUserAgent.isEmpty()) {
            return userAgent + "/" + appendixUserAgent;
        }
        return userAgent;
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }
}
