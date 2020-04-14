package com.kintone.client.model;

import lombok.Data;

/** An object containing information of a saved file. */
@Data
public class FileBody {

    /** The MIME type of the file. */
    private String contentType;

    /** The fileKey of the file. */
    private String fileKey;

    /** The file name of the file. */
    private String name;

    /** The byte size of the file. */
    private Integer size;
}
