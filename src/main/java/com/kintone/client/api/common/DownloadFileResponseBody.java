package com.kintone.client.api.common;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.HttpResponse;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;

/** A response object for Download File API. */
@RequiredArgsConstructor
public class DownloadFileResponseBody implements KintoneResponseBody, AutoCloseable {
    private final HttpResponse response;

    /**
    * Returns the size of content in bytes.
    *
    * @return the content length
    */
    public Long getContentLength() {
        return response.getContentLength();
    }

    /**
    * Returns the MIME type of specified file.
    *
    * @return the MIME type of file
    */
    public String getContentType() {
        return response.getContentType();
    }

    /**
    * Gets the content of the file.
    *
    * @return the content data stream
    * @throws IOException if an I/O error occurs
    */
    public InputStream getContent() throws IOException {
        return response.getContent();
    }

    /**
    * Close this resource.
    *
    * @throws IOException if an I/O error occurs
    */
    @Override
    public void close() throws IOException {
        response.close();
    }
}
