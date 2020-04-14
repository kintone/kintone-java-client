package com.kintone.client.model;

import lombok.Value;

/** An object representing a Group value. */
@Value
public class Group {

    /** The name of group. */
    private final String name;

    /** The code of group. */
    private final String code;

    /**
    * Constructor to create a group value used for editing Group Selection fields. When adding or
    * updating values of Group Selection field, the "name" parameter will be ignored. This
    * constructor only sets the "code" field while leaves the "name" field empty.
    *
    * @param code the code of the group.
    */
    public Group(String code) {
        this("", code);
    }

    public Group(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
