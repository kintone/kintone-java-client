package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.FieldRight;
import java.util.List;
import lombok.Value;

/** A response object for Get Field Acl API. */
@Value
public class GetFieldAclResponseBody implements KintoneResponseBody {

    /** A list of objects that contain data of permission settings. */
    private final List<FieldRight> rights;

    /** The revision number of the App settings. */
    private final long revision;
}
