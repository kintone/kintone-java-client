package com.kintone.client.model;

import java.beans.ConstructorProperties;
import lombok.Value;

/** An object representing a Department value. */
@Value
public class Organization {

    /** The name of department. */
    private final String name;

    /** The code of department. */
    private final String code;

    /**
     * Constructor to create a department value used for editing Department Selection fields. When
     * adding or updating values of Department Selection field, the "name" parameter will be ignored.
     * This constructor only sets the "code" field while leaves the "name" field empty.
     *
     * @param code the code of the department.
     */
    public Organization(String code) {
        this("", code);
    }

    @ConstructorProperties({"name", "code"})
    public Organization(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
