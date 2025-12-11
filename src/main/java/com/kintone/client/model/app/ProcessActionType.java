package com.kintone.client.model.app;

public enum ProcessActionType {

    /** Only assignees can execute this action. */
    PRIMARY,

    /** Users other than assignees can also execute this action. */
    SECONDARY
}
