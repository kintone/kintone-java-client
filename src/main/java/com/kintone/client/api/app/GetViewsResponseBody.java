package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.View;
import java.util.Map;
import lombok.Value;

/** A response object for Get Views API. */
@Value
public class GetViewsResponseBody implements KintoneResponseBody {

    /** An object listing View information. */
    private final Map<String, View> views;

    /** The revision number of the App settings. */
    private final long revision;
}
