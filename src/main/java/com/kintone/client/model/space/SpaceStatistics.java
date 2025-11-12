package com.kintone.client.model.space;

import com.kintone.client.model.User;
import lombok.Value;

/** A model object for space statistics. */
@Value
public class SpaceStatistics {

    /** The Space ID. */
    private final long id;

    /** The name of the Space. */
    private final String name;

    /** The number of administrators of the Space. */
    private final long administratorCount;

    /** The number of members of the Space. */
    private final long memberCount;

    /**
     * The "Private" settings of the Space.
     *
     * @return true if the Space is private.
     */
    private final boolean isPrivate;

    /**
     * The Guest Space setting.
     *
     * @return true if the Space is a Guest Space.
     */
    private final boolean isGuest;

    /** An object containing information of the creator of the Space. */
    private final User creator;

    /** The created date time of the Space. */
    private final String createdAt;

    /** An object containing information of the updater of the Space. */
    private final User modifier;

    /** The updated date time of the Space. */
    private final String modifiedAt;
}
