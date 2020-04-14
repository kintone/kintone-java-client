package com.kintone.client.model.app;

import lombok.Data;

/** Status settings information for getting and updating the Process Management Settings. */
@Data
public class ProcessState {

    /** The status name. */
    private String name;

    /** The display order (ascending) of the Status, when listed with the other statuses. */
    private String index;

    /** An object containing data of the Assignee settings. */
    private ProcessAssignee assignee;
}
