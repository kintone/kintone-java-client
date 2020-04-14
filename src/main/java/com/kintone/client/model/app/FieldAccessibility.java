package com.kintone.client.model.app;

import lombok.Getter;

/** An enum representing permissions of a field granted to the entity. */
@Getter
public enum FieldAccessibility {

    /** Permissions to view and edit the field. */
    WRITE(true, true),

    /** Permissions to view the field only. */
    READ(true, false),

    /** No permissions to view and edit the field. */
    NONE(false, false);

    private final boolean readable;
    private final boolean writable;

    FieldAccessibility(boolean readable, boolean writable) {
        this.readable = readable;
        this.writable = writable;
    }
}
