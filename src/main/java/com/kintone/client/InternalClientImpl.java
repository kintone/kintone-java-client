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
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.net.ssl.SSLContext;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

class InternalClientImpl extends InternalClient {

    private final CloseableHttpClient httpClient;
    private final String baseUrl;
    private final Auth auth;
    private final BasicAuth basicAuth;
    private final Long guestId;
    private final String userAgent;
    private final JsonMapper mapper;

    InternalClientImpl(
            String baseUrl,
            Auth auth,
            BasicAuth basicAuth,
            URI proxyHost,
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
        this.httpClient =
                createHttpClient(
                        sslContext, proxyHost, connectionTimeout, socketTimeout, connectionRequestTimeout);
    }

    private static CloseableHttpClient createHttpClient(
            SSLContext sslContext,
            URI proxyHost,
            int connTimeout,
            int socketTimeout,
            int connRequestTimeout) {
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        configBuilder.setConnectTimeout(connTimeout);
        configBuilder.setSocketTimeout(socketTimeout);
        configBuilder.setConnectionRequestTimeout(connRequestTimeout);

        if (proxyHost != null) {
            HttpHost proxy =
                    new HttpHost(proxyHost.getHost(), proxyHost.getPort(), proxyHost.getScheme());
            configBuilder.setProxy(proxy);
        }

        HttpClientBuilder clientBuilder = HttpClients.custom();
        clientBuilder.setDefaultRequestConfig(configBuilder.build());
        clientBuilder.setSSLContext(sslContext);
        clientBuilder.disableRedirectHandling();
        return clientBuilder.build();
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
        HttpUriRequest request = createJsonRequest(method, path, body);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            KintoneResponse<T> result = parseJsonResponse(response, clazz);
            applyHandlers(result, handlers);
            return result.getBody();
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

    @Override
    BulkRequestsResponseBody bulkRequest(BulkRequestsRequest body, List<ResponseHandler> handlers) {
        String path = getApiPath(KintoneApi.BULK_REQUESTS);

        try {
            HttpUriRequest request =
                    createJsonRequest(
                            KintoneApi.BULK_REQUESTS.getMethod(), path, createBulkRequestBody(body));
            HttpResponse response = httpClient.execute(request);
            KintoneResponse<BulkRequestsResponseBody> resp =
                    parseResponse(
                            response,
                            stream -> {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> responseMap = mapper.parse(stream, Map.class);
                                @SuppressWarnings("unchecked")
                                List<Object> results = (List<Object>) responseMap.get("results");
                                List<KintoneResponseBody> bodies =
                                        parseBulkRequestResponse(body.getRequests(), results);
                                return new BulkRequestsResponseBody(Collections.unmodifiableList(bodies));
                            });

            applyHandlers(resp, handlers);
            return resp.getBody();
        } catch (IOException e) {
            throw new KintoneRuntimeException("Failed to request", e);
        }
    }

    @Override
    DownloadFileResponseBody download(DownloadFileRequest request, List<ResponseHandler> handlers) {
        String path = getApiPath(KintoneApi.DOWNLOAD_FILE);
        HttpUriRequest req = createJsonRequest(KintoneApi.DOWNLOAD_FILE.getMethod(), path, request);
        KintoneResponse<DownloadFileResponseBody> r;
        try {
            CloseableHttpResponse response = httpClient.execute(req);
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
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setContentType(ContentType.MULTIPART_FORM_DATA);

        builder.addPart(
                "file", new InputStreamBody(content, ContentType.create(contentType), filename));

        String headerContentType = "multipart/form-data; boundary=" + boundary;
        String path = getApiPath(KintoneApi.UPLOAD_FILE);
        HttpUriRequest httpRequest =
                createRequest(KintoneApi.UPLOAD_FILE.getMethod(), path, headerContentType, builder.build());
        try (CloseableHttpResponse response = httpClient.execute(httpRequest)) {
            KintoneResponse<UploadFileResponseBody> r =
                    parseJsonResponse(response, UploadFileResponseBody.class);
            applyHandlers(r, handlers);
            return r;
        } catch (IOException e) {
            throw new KintoneRuntimeException("Failed to request", e);
        }
    }

    private static boolean isSuccess(int statusCode) {
        return HttpStatus.SC_OK <= statusCode && statusCode < HttpStatus.SC_MULTIPLE_CHOICES;
    }

    private <T extends KintoneResponseBody> KintoneResponse<T> parseResponse(
            HttpResponse response, Function<InputStream, T> converter) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        Map<String, Object> headers = headersToMap(response.getAllHeaders());
        T result = null;
        String errorBody = null;
        if (isSuccess(statusCode)) {
            result = converter.apply(response.getEntity().getContent());
        } else {
            errorBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        }
        return new KintoneResponse<>(statusCode, headers, result, errorBody);
    }

    private <T extends KintoneResponseBody> KintoneResponse<T> parseJsonResponse(
            HttpResponse response, Class<T> responseClass) throws IOException {
        return parseResponse(response, stream -> mapper.parse(stream, responseClass));
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
        RequestBuilder builder = RequestBuilder.create(method.toString()).setUri(uri);

        for (Map.Entry<String, String> header : auth.getHeaders().entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }

        if (basicAuth != null) {
            String value = basicAuth.getUser() + ":" + basicAuth.getPassword();
            String token = Base64.getEncoder().encodeToString(value.getBytes());
            builder.addHeader("Authorization", "Basic " + token);
        }

        builder.addHeader("User-Agent", userAgent);
        if (entity != null) {
            builder.addHeader("Content-Type", contentType);
        }

        builder.setEntity(entity);
        return builder.build();
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
