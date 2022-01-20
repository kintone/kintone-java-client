package com.kintone.client.model.app;

import com.kintone.client.model.User;
import java.time.ZonedDateTime;
import lombok.Value;

/** General information of an App retrieved by Get Apps API. */
@Value
public class App {

    /** The App ID. */
    private final long appId;

    /** The App Code of the App. */
    private final String code;

    /** The name of the App. */
    private final String name;

    /** The description of the App. */
    private final String description;

    /**
     * If the App was created inside a Space, it will return the Space ID. If not, null is returned.
     */
    private final Long spaceId;

    /**
     * If the App was created inside a Space, it will return the Thread ID of the Thread of the space
     * it belongs to.
     */
    private final Long threadId;

    /** The date of when the App was created. */
    private final ZonedDateTime createdAt;

    /** The information of the user who created the App. */
    private final User creator;

    /** The date of when the App was last modified. */
    private final ZonedDateTime modifiedAt;

    /** The information of the user who last updated the App. */
    private final User modifier;
}
