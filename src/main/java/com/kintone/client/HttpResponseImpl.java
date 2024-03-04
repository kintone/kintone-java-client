package com.kintone.client;

import com.kintone.client.model.HttpResponse;
import java.io.IOException;
import java.io.InputStream;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ClassicHttpResponse;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class HttpResponseImpl implements HttpResponse {
    private final ClassicHttpResponse response;

    @Override
    public Long getContentLength() {
        return response.getEntity().getContentLength();
    }

    @Override
    public String getContentType() {
        return response.getEntity().getContentType();
    }

    @Override
    public InputStream getContent() throws IOException {
        return response.getEntity().getContent();
    }

    @Override
    public void close() throws IOException {
        response.close();
    }
}
