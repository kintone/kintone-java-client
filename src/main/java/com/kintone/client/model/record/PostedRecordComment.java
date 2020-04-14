package com.kintone.client.model.record;

import com.kintone.client.model.Entity;
import com.kintone.client.model.User;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Value;

/** A record comment information retrieved by Get Record Comments API. */
@Value
public class PostedRecordComment {

    /** The comment ID. */
    private final long id;

    /** The comment including the line feed codes. */
    private final String text;

    /** The created date and time of the comment. */
    private final ZonedDateTime createdAt;

    /** An object including information of the comment creator. */
    private final User creator;

    /** A list including information of mentioned users. */
    private final List<Entity> mentions;
}
