package com.kintone.client.api.plugin;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Get Required Plug-ins API. */
@Data
public class GetRequiredPluginsRequest implements KintoneRequest {
    /** The maximum number of plug-ins to retrieve. */
    private Long limit;

    /** The number of plug-ins to skip from the list of plug-ins. */
    private Long offset;
}
