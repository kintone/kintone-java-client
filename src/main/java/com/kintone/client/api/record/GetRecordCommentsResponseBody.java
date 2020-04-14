package com.kintone.client.api.record;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.record.PostedRecordComment;
import java.util.List;
import lombok.Value;

/** A response object for Get Record Comments API. */
@Value
public class GetRecordCommentsResponseBody implements KintoneResponseBody {

    /** A list of comments. */
    private final List<PostedRecordComment> comments;

    /**
    * A boolean that indicates whether there are old comments.
    *
    * @return true if older comments exist.
    */
    private final boolean older;

    /**
    * A boolean that indicates whether there are new comments.
    *
    * @return true if newer comments exist.
    */
    private final boolean newer;
}
