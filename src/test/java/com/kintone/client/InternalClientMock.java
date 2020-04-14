package com.kintone.client;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.api.KintoneResponse;
import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.api.common.BulkRequestsRequest;
import com.kintone.client.api.common.BulkRequestsResponseBody;
import com.kintone.client.api.common.DownloadFileRequest;
import com.kintone.client.api.common.DownloadFileResponseBody;
import com.kintone.client.api.common.UploadFileRequest;
import com.kintone.client.api.common.UploadFileResponseBody;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

class InternalClientMock extends InternalClient {
    private KintoneApi lastApi;
    private Object lastBody;
    private KintoneHttpMethod lastMethod;
    private String lastPath;
    private KintoneResponseBody responseBody;

    public KintoneApi getLastApi() {
        return lastApi;
    }

    public Object getLastBody() {
        return lastBody;
    }

    public KintoneHttpMethod getLastMethod() {
        return lastMethod;
    }

    public String getLastPath() {
        return lastPath;
    }

    public void setResponseBody(KintoneResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    private void setLastApi(KintoneApi api, Object body) {
        lastApi = api;
        lastMethod = null;
        lastPath = null;
        lastBody = body;
    }

    private void setLastApi(KintoneHttpMethod method, String path, Object body) {
        lastApi = null;
        lastMethod = method;
        lastPath = path;
        lastBody = body;
    }

    @Override
    <T extends KintoneResponseBody> T call(
            KintoneApi api, KintoneRequest body, List<ResponseHandler> handlers) {
        setLastApi(api, body);
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) api.getResponseClass();
        return clazz.cast(responseBody);
    }

    @Override
    <T extends KintoneResponseBody> T call(
            KintoneHttpMethod method,
            String path,
            KintoneRequest body,
            Class<T> clazz,
            List<ResponseHandler> handlers) {
        setLastApi(method, path, body);
        @SuppressWarnings("unchecked")
        T response = (T) responseBody;
        return response;
    }

    @Override
    BulkRequestsResponseBody bulkRequest(BulkRequestsRequest body, List<ResponseHandler> handlers) {
        setLastApi(KintoneApi.BULK_REQUESTS, body);
        return (BulkRequestsResponseBody) responseBody;
    }

    @Override
    DownloadFileResponseBody download(DownloadFileRequest request, List<ResponseHandler> handlers) {
        setLastApi(KintoneApi.DOWNLOAD_FILE, request);
        return (DownloadFileResponseBody) responseBody;
    }

    @Override
    KintoneResponse<UploadFileResponseBody> upload(
            String filename, String contentType, InputStream content, List<ResponseHandler> handlers) {
        setLastApi(
                KintoneApi.UPLOAD_FILE,
                new UploadFileRequest()
                        .setFilename(filename)
                        .setContentType(contentType)
                        .setContent(content));
        return new KintoneResponse<>(
                200, Collections.emptyMap(), (UploadFileResponseBody) responseBody, null);
    }

    @Override
    public void close() {}
}
