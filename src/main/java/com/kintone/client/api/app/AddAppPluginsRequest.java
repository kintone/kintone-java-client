package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import java.util.List;
import lombok.Data;

/** A request object for Add App Plug-ins API. */
@Data
public class AddAppPluginsRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** The Plug-in IDs that will be added to the App (required). */
    private List<String> ids;

    /**
     * The expected revision number of the App settings (optional). The request will fail if the
     * revision number is not the latest revision. The revision will not be checked if this parameter
     * is null, or -1 is specified.
     */
    private Long revision;
}
