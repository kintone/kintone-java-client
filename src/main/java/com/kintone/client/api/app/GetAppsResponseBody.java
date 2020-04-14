package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.App;
import java.util.List;
import lombok.Value;

/** A response object for Get Apps API. */
@Value
public class GetAppsResponseBody implements KintoneResponseBody {

    /** The list of objects that contain general information of Apps. */
    private final List<App> apps;
}
