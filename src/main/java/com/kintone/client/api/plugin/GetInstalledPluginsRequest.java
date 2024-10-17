package com.kintone.client.api.plugin;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Get Installed Plugins API. */
@Data
public class GetInstalledPluginsRequest implements KintoneRequest {

    /** The maximum number of plug-ins to retrieve. */
    private Long limit;

    /** The number of plug-ins to skip from the list of installed plug-ins. */
    private Long offset;
}
