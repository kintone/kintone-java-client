package com.kintone.client.model.space;

import java.util.TimeZone;
import lombok.Data;

/** A Guest user information. */
@Data
public class GuestUser {

    /** The email address (log in name) of the Guest user. */
    private String code;

    /** The log in password of the Guest user. */
    private String password;

    /** The timezone of the Guest user. */
    private TimeZone timezone;

    /** The language settings of the Guest user. */
    private String locale;

    /** The profile image of the Guest user. */
    private String image;

    /** The display name of the user. */
    private String name;

    /** The Phonetic Surname settings of the Guest User. */
    private String surNameReading;

    /** The Phonetic Given Name settings of the Guest User. */
    private String givenNameReading;

    /** The Department name to display on the Guest User's profile. */
    private String division;

    /** The Phone number to display on the Guest User's profile. */
    private String phone;

    /** The Skype Name of the Guest user. */
    private String callto;

    /** The Company name to display on the Guest User's profile. */
    private String company;
}
