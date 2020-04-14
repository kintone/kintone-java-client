package com.kintone.client.model.space;

import com.kintone.client.model.Entity;
import java.util.List;
import lombok.Data;

/** An object containing thread comment settings to be posted using Add Thread Comment API. */
@Data
public class ThreadComment {

    /** The comment contents. */
    private String text;

    /** A list of mentioned users, groups or departments, that notify other kintone users. */
    private List<Entity> mentions;

    /** A list of attachment files. */
    private List<ThreadCommentFile> files;
}
