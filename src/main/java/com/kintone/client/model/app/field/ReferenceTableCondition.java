package com.kintone.client.model.app.field;

import lombok.Data;

/** An object containing data of the field mappings for a Related Records field. */
@Data
public class ReferenceTableCondition {

    /** The field code of the field selected for "Field in this app". */
    private String field;

    /** The field code of the field selected for "Field in datasource app". */
    private String relatedField;
}
