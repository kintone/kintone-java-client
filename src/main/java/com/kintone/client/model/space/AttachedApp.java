package com.kintone.client.model.space;

import com.kintone.client.model.User;
import java.time.ZonedDateTime;
import lombok.Value;

/** An object contains information of an App in a Space. */
@Value
public class AttachedApp {

    /** The App ID. */
    private final long appId;

    /** The App Code of the App. */
    private final String code;

    /** The name of the App. */
    private final String name;

    /** The description of the App. */
    private final String description;

    /** The Thread ID of the thread that the App was created in. */
    private final long threadId;

    /** The date of when the App was created. */
    private final ZonedDateTime createdAt;

    /** The information of the user who created the App. */
    private final User creator;

    /** The date of when the App was last updated. */
    private final ZonedDateTime modifiedAt;

    /** The information of the user who last updated the App. */
    private final User modifier;
}
