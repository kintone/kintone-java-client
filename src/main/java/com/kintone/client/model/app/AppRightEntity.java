package com.kintone.client.model.app;

import com.kintone.client.model.Entity;
import lombok.Data;

/** An object that contain data of App permissions for an Entity. */
@Data
public class AppRightEntity {

    /** An object containing data of the entity the permission is granted to. */
    private Entity entity;

    /** The permission inheritance settings of the department the permission is granted to. */
    private Boolean includeSubs;

    /** The App management permission of the entity. */
    private Boolean appEditable;

    /** The record view permission of the entity. */
    private Boolean recordViewable;

    /** The record edit permission of the entity. */
    private Boolean recordEditable;

    /**
    * The record add permission of the entity
    *
    * @return true if records can be added.
    */
    private Boolean recordAddable;

    /** The record delete permission of the entity. */
    private Boolean recordDeletable;

    /**
    * The file import permission of the entity.
    *
    * @return true if files are importable
    */
    private Boolean recordImportable;

    /**
    * The file export permission of the entity.
    *
    * @return true if files are exportable
    */
    private Boolean recordExportable;
}
