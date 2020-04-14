package com.kintone.client.model.app.field;

import java.util.List;
import lombok.Data;

/** An object containing the settings of the Related Records field. */
@Data
public class ReferenceTable {

    /** An object containing data of the "Datasource App" setting. */
    private RelatedApp relatedApp;

    /** An object containing data of the "Fetch Criteria" setting. */
    private ReferenceTableCondition condition;

    /** The "Filter" setting, in a query format. */
    private String filterCond;

    /**
    * A list of field codes of fields specified in the "Datasource App Fields to Display" setting.
    */
    private List<String> displayFields;

    /** The "Display Order" setting, in a query format. */
    private String sort;

    /** The "Max Records to Display at a Time" setting. */
    private Long size;
}
