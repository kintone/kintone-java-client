package com.kintone.client.api.common;

import java.io.InputStream;
import lombok.Data;

/** A request object for Upload File API. */
@Data
public class UploadFileRequest {

    /** The name of the file (required). */
    private String filename;

    /** The MIME type of the file (required). */
    private String contentType;

    /** The content data stream of the file (required). */
    private InputStream content;
}
