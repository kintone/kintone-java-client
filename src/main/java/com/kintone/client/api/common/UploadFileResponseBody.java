package com.kintone.client.api.common;

import com.kintone.client.api.KintoneResponseBody;
import lombok.Value;

/** A response object for Upload File API. */
@Value
public class UploadFileResponseBody implements KintoneResponseBody {

    /** The identifier of the uploaded file. */
    private final String fileKey;
}
