package com.kintone.client.model.app;

import lombok.Data;

/** Action settings information for getting and updating the Process Management Settings. */
@Data
public class ProcessAction {

    /** The Action name. */
    private String name;

    /** The status before taking action. */
    private String from;

    /** The status after taking action. */
    private String to;

    /** The branch criteria of the action. */
    private String filterCond;
}
