package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.ViewId;
import java.util.Map;
import lombok.Value;

/** A response object for Update Views API. */
@Value
public class UpdateViewsResponseBody implements KintoneResponseBody {

    /** An object containing data of the Views. */
    private final Map<String, ViewId> views;

    /** The revision number of the App settings. */
    private final long revision;
}
