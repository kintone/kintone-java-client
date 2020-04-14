package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.app.ProcessAction;
import com.kintone.client.model.app.ProcessState;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** A request object for Update Process Management API. */
@Data
public class UpdateProcessManagementRequest implements KintoneRequest {

    /** The App ID. */
    private Long app;

    /**
    * The on/off settings of the process management settings (optional). If set to null, leaves this
    * setting unchanged.
    */
    private Boolean enable;

    /**
    * An object containing data of the process management statuses (optional). If set to null, leaves
    * this setting unchanged.
    */
    private Map<String, ProcessState> states;

    /**
    * A list containing data of the Actions (optional). If set to null, leaves this setting
    * unchanged.
    */
    private List<ProcessAction> actions;

    /**
    * The expected revision number of the App settings (optional). The request will fail if the
    * revision number is not the latest revision. The revision will not be checked if this parameter
    * is null, or -1 is specified.
    */
    private Long revision;
}
