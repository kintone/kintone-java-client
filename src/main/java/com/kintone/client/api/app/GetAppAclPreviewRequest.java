package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Get App Acl Preview API. */
@Data
public class GetAppAclPreviewRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;
}
