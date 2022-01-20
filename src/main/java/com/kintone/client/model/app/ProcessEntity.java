package com.kintone.client.model.app;

import com.kintone.client.model.Entity;
import lombok.Data;

/** An entity in Assignee settings information. */
@Data
public class ProcessEntity {

    /** An object containing user data of the Assignees. */
    private Entity entity;

    /**
     * The "Include affiliated departments" settings of the department.
     *
     * @return true if affiliated departments are included as Assignees
     */
    private Boolean includeSubs;
}
