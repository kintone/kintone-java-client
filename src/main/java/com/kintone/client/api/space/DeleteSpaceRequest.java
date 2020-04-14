package com.kintone.client.api.space;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Delete Space API. */
@Data
public class DeleteSpaceRequest implements KintoneRequest {

    /** The Space ID (required). */
    private Long id;
}
