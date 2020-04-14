package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.field.FieldProperty;
import java.util.Map;
import lombok.Value;

/** A response object for Get Form Fields API. */
@Value
public class GetFormFieldsResponseBody implements KintoneResponseBody {

    /** An object with data of the field settings. */
    private final Map<String, FieldProperty> properties;

    /** The revision number of the App settings. */
    private final long revision;
}
