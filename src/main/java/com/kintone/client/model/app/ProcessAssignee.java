package com.kintone.client.model.app;

import java.util.List;
import lombok.Data;

/** Assignee settings information for getting and updating the Process Management Settings. */
@Data
public class ProcessAssignee {

    /** The Assignee List type of the Status. */
    private ProcessAssigneeType type;

    /** A list of the Assignees. */
    private List<ProcessEntity> entities;
}
