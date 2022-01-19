package com.kintone.client.model.app;

/** Statuses of the deployment of App settings. */
public enum DeployStatus {

    /** The App settings are being deployed. */
    PROCESSING,

    /** The App settings have been deployed. */
    SUCCESS,

    /** An error occurred, and the deployment of App settings failed. */
    FAIL,

    /**
     * The deployment of App settings was canceled, due to the deployment of other App settings
     * failing.
     */
    CANCEL
}
