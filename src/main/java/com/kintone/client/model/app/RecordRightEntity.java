package com.kintone.client.model.app;

import com.kintone.client.model.Entity;
import lombok.Data;

/** An object representing the entity and the permitted record operations to it. */
@Data
public class RecordRightEntity {

    /** An object containing data of the entity the permissions are granted to. */
    private Entity entity;

    /** The view permission of the entity. */
    private Boolean viewable;

    /** The edit permission of the entity. */
    private Boolean editable;

    /** The delete permission of the entity. */
    private Boolean deletable;

    /** The permission inheritance settings of the department the permissions are granted to. */
    private Boolean includeSubs;
}
