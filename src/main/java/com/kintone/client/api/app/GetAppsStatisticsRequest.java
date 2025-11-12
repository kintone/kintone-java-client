package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Get Apps Statistics API. */
@Data
public class GetAppsStatisticsRequest implements KintoneRequest {

    /** The number of retrievals that will be skipped. */
    private Long offset;

    /** The number of Apps to retrieve. */
    private Long limit;
}
