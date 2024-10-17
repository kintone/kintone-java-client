package com.kintone.client.api.plugin;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Uninstall Plug-in API. */
@Data
public class UninstallPluginRequest implements KintoneRequest {

    /** The ID of the Plug-in (required). */
    private String id;
}
