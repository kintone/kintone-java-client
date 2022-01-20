package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.AppAction;
import java.util.Map;
import lombok.Value;

/** A response object for Get App Action Settings API. */
@Value
public class GetAppActionsResponseBody implements KintoneResponseBody {

    /**
     * An object of Action settings. The object's key is the Action's unique identifier, which is set
     * as the Action's name in its default language settings (this is regardless of the lang request
     * parameter's value). The values of the key are the various Action settings associated with that
     * Action.
     */
    private Map<String, AppAction> actions;

    /** The revision number of the app settings. */
    private final long revision;
}
