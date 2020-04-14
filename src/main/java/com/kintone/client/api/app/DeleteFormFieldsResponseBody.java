package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import lombok.Value;

/** A response object for Delete Form Fields API. */
@Value
public class DeleteFormFieldsResponseBody implements KintoneResponseBody {

    /** The revision number of the App settings. */
    private final long revision;
}
