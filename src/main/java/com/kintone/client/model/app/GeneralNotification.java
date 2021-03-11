package com.kintone.client.model.app;

import com.kintone.client.model.Entity;
import lombok.Data;

/** An object representing the general app notification settings. */
@Data
public class GeneralNotification {
    /**
    * An object containing data of the entity the General Notification settings are configured to.
    */
    private Entity entity;

    /**
    * Option to notify the entity when a record is added.
    *
    * <ul>
    *   <li>true : Notify when a record is added
    *   <li>false : Do not notify when a record is added
    * </ul>
    */
    private Boolean recordAdded;

    /**
    * Option to notify the entity when a record is edited.
    *
    * <ul>
    *   <li>true : Notify when a record is edited
    *   <li>false : Do not notify when a record is edited
    * </ul>
    */
    private Boolean recordEdited;

    /**
    * Option to notify the entity when a comment is posted.
    *
    * <ul>
    *   <li>true : Notify when a comment is posted
    *   <li>false : Do not notify when a comment is posted
    * </ul>
    */
    private Boolean commentAdded;

    /**
    * Option to notify the entity when a status is changed.
    *
    * <ul>
    *   <li>true : Notify when a status is changed
    *   <li>false : Do not notify when a status is changed
    * </ul>
    */
    private Boolean statusChanged;

    /**
    * Option to notify the entity when a file is imported.
    *
    * <ul>
    *   <li>true : Notify when a file is imported
    *   <li>false : Do not notify when a file is imported
    * </ul>
    */
    private Boolean fileImported;

    /**
    * The "Include affiliated departments" setting of the Department. Will always return false unless
    * the notifications[].entity.type is set to ORGANIZATION or FIELD_ENTITY for a Department
    * Selection Field.
    *
    * <ul>
    *   <li>true : Affiliated departments do inherit the General Notification settings
    *   <li>false : Affiliated departments do not inherit the General Notification settings
    * </ul>
    */
    private Boolean includeSubs;
}
