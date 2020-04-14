package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Get Field Acl Preview API. */
@Data
public class GetFieldAclPreviewRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;
}
