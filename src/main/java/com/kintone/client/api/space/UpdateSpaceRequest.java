package com.kintone.client.api.space;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.space.SpacePermissions;
import lombok.Data;

/** A request object for Update Space API. */
@Data
public class UpdateSpaceRequest implements KintoneRequest {

    /** The Space ID (required). */
    private Long id;

    /** The name of the Space (optional). */
    private String name;

    /** The "Private" settings of the Space (optional). */
    private Boolean isPrivate;

    /**
     * The "Enable multiple threads." setting (optional). If this parameter is ignored or <code>false
     * </code> is specified, this parameter will not be updated.
     */
    private Boolean useMultiThread;

    /**
     * The "Block users from joining or leaving the space and following or unfollowing the threads."
     * setting (optional).
     */
    private Boolean fixedMember;

    /** Whether the "Announcement" widget in the Space Portal page is shown (optional). */
    private Boolean showAnnouncement;

    /** Whether the "Apps" widget in the Space Portal page is shown (optional). */
    private Boolean showAppList;

    /** Whether the "People" widget in the Space Portal page is shown (optional). */
    private Boolean showMemberList;

    /** Whether the "Threads" widget in the Space Portal page is shown (optional). */
    private Boolean showThreadList;

    /**
     * Whether the "Related Apps &amp; Spaces" widget in the Space Portal page is shown (optional).
     */
    private Boolean showRelatedLinkList;

    /** An object contains information of permissions of the Space (optional). */
    private SpacePermissions permissions;
}
