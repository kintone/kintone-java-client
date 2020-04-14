package com.kintone.client.model.space;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kintone.client.model.Entity;
import lombok.Data;

/** A Space member information. */
@Data
public class SpaceMember {

    /** The entity information of the Space member. */
    private Entity entity;

    /** The Space Administration settings of the user. */
    @JsonProperty("isAdmin")
    private Boolean isAdmin;

    /** The "Include Affiliated Departments" settings of the department. */
    private Boolean includeSubs;
}
