package com.kintone.client.model;

import lombok.Value;

/**
 * An {@link Entity} represents the user-like object. Specifically, an user, group and department.
 * This class is used to specify users, groups and organizations for setting the initial values of
 * an User Selection field, the subject of access controls, and so on.
 */
@Value
public class Entity {
    /** The type of the entity: USER, GROUP or ORGANIZATION etc. */
    private final EntityType type;

    /** The code of the entity the permission is granted to. */
    private final String code;
}
