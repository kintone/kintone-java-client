package com.kintone.client.api.space;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Get Space API. */
@Data
public class GetSpaceRequest implements KintoneRequest {

    /** The Space ID (required). */
    private Long id;
}
