package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import java.util.List;
import lombok.Data;

/** A request object for Get Apps API. */
@Data
public class GetAppsRequest implements KintoneRequest {

    /** The App IDs. */
    private List<Long> ids;

    /** The App Code. */
    private List<String> codes;

    /** The App Name. */
    private String name;

    /** The Space ID of where the App resides in. */
    private List<Long> spaceIds;

    /** The number of Apps to retrieve. */
    private Long limit;

    /** The number of retrievals that will be skipped. */
    private Long offset;
}
