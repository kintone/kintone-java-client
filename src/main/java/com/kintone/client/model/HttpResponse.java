package com.kintone.client.model;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/** An interface that represents a HTTP response. */
public interface HttpResponse extends Closeable {

    /**
     * Returns the size of content in bytes.
     *
     * @return the content length
     */
    Long getContentLength();

    /**
     * Returns the MIME type of the response.
     *
     * @return the MIME type
     */
    String getContentType();

    /**
     * Gets the content of the response.
     *
     * @return the content data stream
     * @throws IOException if an I/O error occurs
     */
    InputStream getContent() throws IOException;
}
