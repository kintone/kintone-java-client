package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Get App Acl API. */
@Data
public class GetAppAclRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;
}
