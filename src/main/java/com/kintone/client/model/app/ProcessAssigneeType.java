package com.kintone.client.model.app;

public enum ProcessAssigneeType {

    /** User chooses one assignee from the list to take action */
    ONE,

    /** All assignees in the list must take action */
    ALL,

    /** One assignee in the list must take action */
    ANY
}
