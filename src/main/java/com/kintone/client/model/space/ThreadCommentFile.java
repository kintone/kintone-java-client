package com.kintone.client.model.space;

import lombok.Data;

/** An object containing information of a file that will be attached to a thread comment. */
@Data
public class ThreadCommentFile {

    /** The fileKey of the attachment file. */
    private String fileKey;

    /** A width can be specified if the attachment file is an image. */
    private Integer width;
}
