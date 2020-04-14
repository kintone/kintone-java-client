package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import lombok.Value;

/** A response object for Add App API. */
@Value
public class AddAppResponseBody implements KintoneResponseBody {

    /** The App ID of the created preview App. */
    private final long app;

    /** The revision number of the App settings. */
    private final long revision;
}
