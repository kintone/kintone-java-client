package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.app.RecordRight;
import java.util.List;
import lombok.Data;

/** A request object for Update Record Acl API. */
@Data
public class UpdateRecordAclRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** The list of record permissions (required). */
    private List<RecordRight> rights;

    /**
     * The expected revision number of the App settings (optional). The request will fail if the
     * revision number is not the latest revision. The revision will not be checked if this parameter
     * is null, or -1 is specified.
     */
    private Long revision;
}
