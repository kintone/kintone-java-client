package com.kintone.client.api.plugin;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.plugin.App;
import java.util.List;
import lombok.Value;

/** A response object for Get Apps API. */
@Value
public class GetAppsResponseBody implements KintoneResponseBody {

    /**
     * A list of objects containing the App ID and name. Objects are listed in ascending order of
     * their App IDs.
     */
    private final List<App> apps;
}
