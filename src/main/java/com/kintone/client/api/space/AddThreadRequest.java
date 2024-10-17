package com.kintone.client.api.space;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Add Thread API. */
@Data
public class AddThreadRequest implements KintoneRequest {

    /** The Space ID (required). */
    private Long space;

    /** The name of the new Thread (required). */
    private String name;
}
