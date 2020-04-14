package com.kintone.client.api.common;

import lombok.Data;

/** A request object for Download File API. */
@Data
public class DownloadFileRequest {

    /** The identifier of the file to download (required). */
    private String fileKey;
}
