package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.AppStatistics;
import java.util.List;
import lombok.Value;

/** A response object for Get Apps Statistics API. */
@Value
public class GetAppsStatisticsResponseBody implements KintoneResponseBody {

    /** The list of objects that contain usage statistics information of Apps. */
    private final List<AppStatistics> apps;
}
