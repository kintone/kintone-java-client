package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.app.layout.Layout;
import java.util.List;
import lombok.Data;

/** A request object for Update Form Layout API. */
@Data
public class UpdateFormLayoutRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** A list of field layouts for each row (required). */
    private List<Layout> layout;

    /**
     * The expected revision number of the App settings (optional). The request will fail if the
     * revision number is not the latest revision. The revision will not be checked if this parameter
     * is null, or -1 is specified.
     */
    private Long revision;
}
