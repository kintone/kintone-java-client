package com.kintone.client.model.app;

import com.kintone.client.model.Entity;
import lombok.Data;

/** An object representing a notification recipient setting. */
@Data
public class NotificationTarget {

    /** An object containing entity details per recipient of the notification. */
    private Entity entity;

    /**
     * The "Include affiliated departments" setting of the Department.
     *
     * <ul>
     *   <li>true: Affiliated departments do inherit this notification settings
     *   <li>false: Affiliated departments do not inherit this notification settings
     * </ul>
     */
    private Boolean includeSubs;
}
