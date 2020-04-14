package com.kintone.client.api.space;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.space.SpaceMember;
import java.util.List;
import lombok.Data;

/** A request object for Update Space Members API. */
@Data
public class UpdateSpaceMembersRequest implements KintoneRequest {

    /** The Space ID (required). */
    private Long id;

    /** A list of members of the Space (required). */
    private List<SpaceMember> members;
}
