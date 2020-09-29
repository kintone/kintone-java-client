package com.kintone.client.model;

import java.beans.ConstructorProperties;
import lombok.Value;

/** An object representing an User value. */
@Value
public class User {

    /** The display name of the user. */
    private final String name;

    /** The code (log in name) of the user. */
    private final String code;

    /**
    * Constructor to create a user value used for editing User Selection fields. When adding or
    * updating values of User Selection field, the "name" parameter will be ignored. This constructor
    * only sets the "code" field while leaves the "name" field empty.
    *
    * @param code the code of the user.
    */
    public User(String code) {
        this("", code);
    }

    @ConstructorProperties({"name", "code"})
    public User(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
