package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.app.DeployApp;
import java.util.List;
import lombok.Data;

/** A request object for Deploy App API. */
@Data
public class DeployAppRequest implements KintoneRequest {

    /** The list of Apps to deploy the pre-live settings to the live Apps (required). */
    private List<DeployApp> apps;

    /** Specify "true" to cancel all changes made to the pre-live settings (optional). */
    private Boolean revert;
}
