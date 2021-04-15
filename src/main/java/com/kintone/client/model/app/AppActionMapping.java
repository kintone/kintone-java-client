package com.kintone.client.model.app;

import lombok.Data;

/** An object containing the "Field Mappings" option of the Action settings. */
@Data
public class AppActionMapping {

    /** The type of source data that is to be copied. */
    private AppActionSourceType srcType;

    /**
    * The field code of the field specified in the "Field Mappings" options as the source.
    *
    * <p>In the response of Get Action Settings API, this parameter is returned only if the {@link
    * #srcType} parameter is set to "FIELD".
    */
    private String srcField;

    /** The field code of the field specified in the "Field Mappings" options as the destination. */
    private String destField;
}
