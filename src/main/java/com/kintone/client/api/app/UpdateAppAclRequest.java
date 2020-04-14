package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.app.AppRightEntity;
import java.util.List;
import lombok.Data;

/** A request object for Update App Acl API. */
@Data
public class UpdateAppAclRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** The list of App permissions, in order of priority (required). */
    private List<AppRightEntity> rights;

    /**
    * The expected revision number of the App settings (optional). The request will fail if the
    * revision number is not the latest revision. The revision will not be checked if this parameter
    * is null, or -1 is specified.
    */
    private Long revision;
}
