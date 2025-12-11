package com.kintone.client.model.app;

/** The type of Action in Process Management Settings. */
public enum ProcessActionType {

    /** Only assignees can execute this action. */
    PRIMARY,

    /** Users other than assignees can also execute this action. */
    SECONDARY
}
