package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import lombok.Value;

/** A response object for Update Form Layout API. */
@Value
public class UpdateFormLayoutResponseBody implements KintoneResponseBody {

    /** The revision number of the App settings. */
    private final long revision;
}
