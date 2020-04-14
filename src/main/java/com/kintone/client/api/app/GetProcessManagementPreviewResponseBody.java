package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.ProcessAction;
import com.kintone.client.model.app.ProcessState;
import java.util.List;
import java.util.Map;
import lombok.Value;

/** A response object for Get Process Management Preview API. */
@Value
public class GetProcessManagementPreviewResponseBody implements KintoneResponseBody {

    /** The on/off settings of the process management settings. */
    private final boolean enable;

    /** An object containing data of the process management statuses. */
    private final Map<String, ProcessState> states;

    /** A list containing data of the Actions. */
    private final List<ProcessAction> actions;

    /** The revision number of the App settings. */
    private final long revision;
}
