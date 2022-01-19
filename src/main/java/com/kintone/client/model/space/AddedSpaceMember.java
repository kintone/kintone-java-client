package com.kintone.client.model.space;

import com.kintone.client.model.Entity;
import lombok.Value;

/** A Space member information retrieved by Get Space Members API. */
@Value
public class AddedSpaceMember {

    /** The entity information of the Space member. */
    private final Entity entity;

    /**
     * The Space Admin settings of the Space member
     *
     * @return true if the Space Member is the Space Administrator.
     */
    private final boolean isAdmin;

    /** The "Include Affiliated Departments" setting of the Department Space Member. */
    private final boolean includeSubs;

    /** If the Space Member is added as a User or not. */
    private final boolean isImplicit;
}
