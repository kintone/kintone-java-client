package com.kintone.client.model.app;

import lombok.Value;

/** An information of App's deployment status. */
@Value
public class AppDeployStatus {

    /** The App ID. */
    private final long app;

    /** The status of the deployment of App settings. */
    private final DeployStatus status;
}
