package com.kintone.client.model.app;

import com.kintone.client.model.Entity;
import lombok.Data;

/** An object representing the entity and the permitted field operations to it. */
@Data
public class FieldRightEntity {

    /** The permission granted to the entity. */
    private FieldAccessibility accessibility;

    /** An object containing data of the entity the permission is granted to. */
    private Entity entity;

    /** The permission inheritance settings of the department the permission is granted to. */
    private Boolean includeSubs;
}
