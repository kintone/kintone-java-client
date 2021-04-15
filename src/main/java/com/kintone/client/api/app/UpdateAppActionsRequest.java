package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.app.AppAction;
import java.util.Map;
import lombok.Data;

/** A request object for Update App Actions Settings API. */
@Data
public class UpdateAppActionsRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /**
    * An object listing Action settings. The object's key is the Action's unique identifier, which is
    * equal to the Action's name in its default language settings. The values of the key are the
    * various Action settings associated with that Action.
    */
    private Map<String, AppAction> actions;

    /**
    * The expected revision number of the App settings (optional). The request will fail if the
    * revision number is not the latest revision. The revision will not be checked if this parameter
    * is null, or -1 is specified.
    */
    private Long revision;
}
