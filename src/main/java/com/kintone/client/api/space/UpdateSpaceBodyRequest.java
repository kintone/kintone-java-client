package com.kintone.client.api.space;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Update Space Body API. */
@Data
public class UpdateSpaceBodyRequest implements KintoneRequest {

    /** The Space ID (required). */
    private Long id;

    /** The contents of the body as an HTML string (required). */
    private String body;
}
