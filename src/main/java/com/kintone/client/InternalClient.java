package com.kintone.client;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.api.KintoneResponse;
import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.api.common.BulkRequestsRequest;
import com.kintone.client.api.common.BulkRequestsResponseBody;
import com.kintone.client.api.common.DownloadFileRequest;
import com.kintone.client.api.common.DownloadFileResponseBody;
import com.kintone.client.api.common.UploadFileResponseBody;
import java.io.Closeable;
import java.io.InputStream;
import java.util.List;

abstract class InternalClient implements Closeable {
    abstract <T extends KintoneResponseBody> T call(
            KintoneApi api, KintoneRequest body, List<ResponseHandler> handlers);

    abstract <T extends KintoneResponseBody> T call(
            KintoneHttpMethod method,
            String path,
            KintoneRequest body,
            Class<T> clazz,
            List<ResponseHandler> handlers);

    abstract BulkRequestsResponseBody bulkRequest(
            BulkRequestsRequest body, List<ResponseHandler> handlers);

    abstract DownloadFileResponseBody download(
            DownloadFileRequest request, List<ResponseHandler> handlers);

    abstract KintoneResponse<UploadFileResponseBody> upload(
            String filename, String contentType, InputStream content, List<ResponseHandler> handlers);
}
