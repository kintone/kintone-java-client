package com.kintone.client.api.space;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.space.AddedSpaceMember;
import java.util.List;
import lombok.Value;

/** A response object for Get Space Members API. */
@Value
public class GetSpaceMembersResponseBody implements KintoneResponseBody {

    /** A list of Space members. */
    private final List<AddedSpaceMember> members;
}
