package com.kintone.client.model.app;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The status of App settings. */
public enum AppStatisticsStatus {
    /** The app settings are changed but not activated yet. */
    @JsonProperty("CHANGED")
    CHANGED,

    /** The app is not activated yet. */
    @JsonProperty("NOT_ACTIVATED")
    NOT_ACTIVATED,

    /** The app is activated. */
    @JsonProperty("ACTIVATED")
    ACTIVATED
}
