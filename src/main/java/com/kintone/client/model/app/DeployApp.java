package com.kintone.client.model.app;

import lombok.Data;

/** An information of the App to deploy the pre-live settings to the live Apps. */
@Data
public class DeployApp {

    /** The App ID (required). */
    private Long app;

    /** Specify the revision number of the settings that will be deployed (optional). */
    private Long revision;
}
