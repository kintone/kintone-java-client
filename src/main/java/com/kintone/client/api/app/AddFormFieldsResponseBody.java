package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import lombok.Value;

/** A response object for Add Form Fields API. */
@Value
public class AddFormFieldsResponseBody implements KintoneResponseBody {

    /** The revision number of the App settings. */
    private final long revision;
}
