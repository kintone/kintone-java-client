package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.ActionId;
import java.util.Map;
import lombok.Value;

/** A response object for Update App Actions Settings API. */
@Value
public class UpdateAppActionsResponseBody implements KintoneResponseBody {

    /** An object containing data of the Action settings. */
    private Map<String, ActionId> actions;

    /** The revision number of the App settings. */
    private final long revision;
}
