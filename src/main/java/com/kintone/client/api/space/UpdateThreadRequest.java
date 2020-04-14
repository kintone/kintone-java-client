package com.kintone.client.api.space;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Update Thread API. */
@Data
public class UpdateThreadRequest implements KintoneRequest {

    /** The Thread ID (required). */
    private Long id;

    /**
    * The new name of the Thread (optional). The name will not be updated if this parameter is null.
    */
    private String name;

    /** The contents of the Thread body (optional). If set to null, leaves this setting unchanged. */
    private String body;
}
