package com.kintone.client.model.app;

import java.util.List;
import lombok.Data;

/** An object containing settings of an App view. */
@Data
public class View {

    /** The View ID. */
    private Long id;

    /** The type of View. */
    private ViewType type;

    /** The type of the built-in View. */
    private BuiltinType builtinType;

    /** The name of the View. */
    private String name;

    /** Used for List Views. */
    private List<String> fields;

    /** The filter condition in a query format. */
    private String filterCond;

    /** The sort order in a query format. */
    private String sort;

    /** The display order of the View, in the list of views, specified with a number. */
    private Long index;

    /** Used for Calendar views. */
    private String title;

    /** Used for Calendar Views. */
    private String date;

    /** Used for Custom Views. */
    private String html;

    /** The scope of where the view is displayed. */
    private Device device;

    /**
     * The pagination settings. Specify one of the following:
     *
     * @return true if enable (default)
     */
    private Boolean pager;
}
