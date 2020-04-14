package com.kintone.client.api.space;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.User;
import com.kintone.client.model.space.AttachedApp;
import com.kintone.client.model.space.CoverType;
import java.util.List;
import lombok.Value;

/** A response object for Get Space API. */
@Value
public class GetSpaceResponseBody implements KintoneResponseBody {

    /** The Space ID. */
    private final long id;

    /** The name of the Space */
    private final String name;

    /** The Thread ID of the default thread that was created when the Space was made. */
    private final long defaultThread;

    /**
    * The "Private" settings of the Space.
    *
    * @return true if the Space is private.
    */
    private final boolean isPrivate;

    /** An object containing information of the creator of the Space. */
    private final User creator;

    /** An object containing information of the updater of the Space. */
    private final User modifier;

    /** The image type of the Cover Photo. */
    private final CoverType coverType;

    /** The key of the Cover Photo. */
    private final String coverKey;

    /** The URL of the Cover Photo. */
    private final String coverUrl;

    /** The HTML of the Space body. */
    private final String body;

    /** A list of Apps that are in the thread. */
    private final List<AttachedApp> attachedApps;

    /** The number of members of the Space. */
    private final long memberCount;

    /**
    * The "Enable multiple threads." setting.
    *
    * @return true if the Space is a multi-threaded Space.
    */
    private final boolean useMultiThread;

    /**
    * The Guest Space setting.
    *
    * @return true if the Space is a Guest Space.
    */
    private final boolean isGuest;

    /**
    * The "Block users from joining or leaving the space and following or unfollowing the threads."
    * setting.
    *
    * @return true if users cannot join/leave the Space or follow/unfollow threads.
    */
    private final boolean fixedMember;
}
