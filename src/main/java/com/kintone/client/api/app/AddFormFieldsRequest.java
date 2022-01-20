package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.app.field.FieldProperty;
import java.util.Map;
import lombok.Data;

/** A request object for Add Form Fields API. */
@Data
public class AddFormFieldsRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /**
     * The expected revision number of the App settings (optional). The request will fail if the
     * revision number is not the latest revision. The revision will not be checked if this parameter
     * is null, or -1 is specified.
     */
    private Long revision;

    /**
     * An object with data of the field settings (required). A key is a field code and the value is
     * the properties of the field.
     */
    private Map<String, FieldProperty> properties;
}
