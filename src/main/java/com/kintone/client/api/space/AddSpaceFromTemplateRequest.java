package com.kintone.client.api.space;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.space.SpaceMember;
import java.util.List;
import lombok.Data;

/** A request object for Add Space From Template API. */
@Data
public class AddSpaceFromTemplateRequest implements KintoneRequest {

    /** The Space Template ID (required). */
    private Long id;

    /** The name of the Space (required). */
    private String name;

    /** A list of members of the Space (required). */
    private List<SpaceMember> members;

    /** The Space is a Private Space or not (optional). */
    private Boolean isPrivate;

    /** true to make the Space as a Guest Space (optional). */
    private Boolean isGuest;

    /**
    * If true, users will not be able to join/leave the Space or follow/unfollow threads (optional).
    */
    private Boolean fixedMember;
}
