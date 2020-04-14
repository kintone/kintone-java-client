package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import lombok.Value;

/** A response object for Update App Acl API. */
@Value
public class UpdateAppAclResponseBody implements KintoneResponseBody {

    /** The revision number of the App settings. */
    private final long revision;
}
