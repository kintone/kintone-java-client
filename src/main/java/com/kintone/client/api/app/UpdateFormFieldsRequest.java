package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.app.field.FieldProperty;
import java.util.Map;
import lombok.Data;

/** A request object for Update Form Fields API. */
@Data
public class UpdateFormFieldsRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** An object with data of the field settings (required). */
    private Map<String, FieldProperty> properties;

    /**
     * The expected revision number of the App settings (optional). The request will fail if the
     * revision number is not the latest revision. The revision will not be checked if this parameter
     * is null, or -1 is specified.
     */
    private Long revision;
}
