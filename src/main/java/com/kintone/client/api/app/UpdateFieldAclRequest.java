package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.app.FieldRight;
import java.util.List;
import lombok.Data;

/** A request object for Update Field Acl API. */
@Data
public class UpdateFieldAclRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** A list containing data of field permissions (required). */
    private List<FieldRight> rights;

    /**
    * The expected revision number of the App settings (optional). The request will fail if the
    * revision number is not the latest revision. The revision will not be checked if this parameter
    * is null, or -1 is specified.
    */
    private Long revision;
}
