package com.kintone.client.model;

/** The type of {@link Entity} */
public enum EntityType {

    /** The entity represents an Kintone user. */
    USER,

    /** The entity represents a group on Kintone. */
    GROUP,

    /** The entity represents a department on Kintone. */
    ORGANIZATION,

    /**
     * The entity represents the creator of a record. This value is only used/valid for some APIs such
     * as Get/Update App Permissions etc.
     */
    CREATOR,

    /**
     * This value means "refer the value of specified User/Group/Department selection field". This
     * value is only used/valid for some APIs such as Get/Update Record Permissions etc.
     */
    FIELD_ENTITY,

    /**
     * This value means "refer the value of Custom Field of the user profile". This value is only
     * used/valid for Get/Update Process Management Settings APIs.
     */
    CUSTOM_FIELD,

    /**
     * This entity represents functions such as "LOGINUSER()" for the user selection field and
     * "PRIMARY_ORGANIZATION()" for the organization selection field.
     */
    FUNCTION
}
