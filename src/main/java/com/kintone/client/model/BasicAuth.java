package com.kintone.client.model;

import lombok.Value;

/** A container object for the user and password of BASIC Authentication. */
@Value
public class BasicAuth {

    /** The user of BASIC Authentication. */
    private final String user;

    /** The password of BASIC Authentication. */
    private final String password;
}
