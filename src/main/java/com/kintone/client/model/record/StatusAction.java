package com.kintone.client.model.record;

import lombok.Data;

/** An object that stores information for updating a record status. */
@Data
public class StatusAction {

    /** The Record ID (required). */
    private Long id;

    /** The Action name of the action to run (required). */
    private String action;

    /** The next Assignee (optional). */
    private String assignee;

    /** The revision number of the record before updating the status (optional). */
    private Long revision;
}
