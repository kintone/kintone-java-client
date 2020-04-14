package com.kintone.client.model.record;

import com.kintone.client.model.Entity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** A record comment information to be posted by Add Record Comment API. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordComment {

    /** The comment text (required). */
    private String text;

    /** A list including information to mention other users (optional). */
    private List<Entity> mentions;

    public RecordComment(String text) {
        this(text, null);
    }
}
