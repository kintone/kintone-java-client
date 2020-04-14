package com.kintone.client.model.app.field;

import lombok.Data;

/** The "Field Mappings" setting of a Lookup field. */
@Data
public class FieldMapping {
    /** The field code of the field set for the mapping endpoint. */
    private String field;

    /** The field code of the field selected for "Field in datasource app". */
    private String relatedField;
}
