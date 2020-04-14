package com.kintone.client.model.app.field;

import java.util.List;
import lombok.Data;

/** An object containing the settings of the Lookup field. */
@Data
public class LookupSetting {
    /** An object that represents the settings for the related app. */
    private RelatedApp relatedApp;

    /** The field code specified in source field. */
    private String relatedKeyField;

    /** A list of data set for the "Field Mappings" setting. */
    private List<FieldMapping> fieldMappings;

    /** A list of field codes of fields set for the "Fields Shown in Lookup Picker" setting. */
    private List<String> lookupPickerFields;

    /** The default filter query for the "Filter" setting. */
    private String filterCond;

    /** The default sort order for the "Filter" setting. */
    private String sort;
}
