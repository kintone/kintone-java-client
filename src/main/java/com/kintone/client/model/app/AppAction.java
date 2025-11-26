package com.kintone.client.model.app;

import com.kintone.client.model.Entity;
import com.kintone.client.model.app.field.RelatedApp;
import java.util.List;
import lombok.Data;

/** An object containing an Action setting. */
@Data
public class AppAction {

    /** The name of the Action. */
    private String name;

    /** The ID of the Action. */
    private Long id;

    /** The order of the Action, starting from 0. */
    private Long index;

    /**
     * An object containing the Target option that specifies the destination app where data is to be
     * copied.
     */
    private RelatedApp destApp;

    /** An array of objects containing the "Field Mappings" options. */
    private List<AppActionMapping> mappings;

    /** An array of objects containing the entities the Action is granted to. */
    private List<Entity> entities;

    /**
     * A query string for the conditions under which the Action can be executed. If an empty string is
     * specified, the Action can be executed for all records.
     */
    private String filterCond;
}
