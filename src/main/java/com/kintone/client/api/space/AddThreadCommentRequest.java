package com.kintone.client.api.space;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.space.ThreadComment;
import lombok.Data;

/** A request object for Add Thread Comment API. */
@Data
public class AddThreadCommentRequest implements KintoneRequest {

    /** The Space ID (required). */
    private Long space;

    /** The Thread ID (required). */
    private Long thread;

    /** An object including comment details (required). */
    private ThreadComment comment;
}
