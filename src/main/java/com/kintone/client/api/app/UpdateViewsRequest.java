package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.app.View;
import java.util.Map;
import lombok.Data;

/** A request object for Update Views API. */
@Data
public class UpdateViewsRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /**
    * An object of data of Views (required).
    *
    * <p>A key is the view name and the value is its view settings. Update Views API will replace all
    * current Views with the Views listed in the request. View names that are not stated in this map
    * will be deleted.
    */
    private Map<String, View> views;

    /**
    * The expected revision number of the App settings (optional). The request will fail if the
    * revision number is not the latest revision. The revision will not be checked if this parameter
    * is null, or -1 is specified.
    */
    private Long revision;
}
