package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.app.report.Report;
import java.util.Map;
import lombok.Data;

/** A request object for Update Graphs API. */
@Data
public class UpdateReportsRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /**
    * An object listing Graph information (required).
    *
    * <p>The key of the object is the graph's unique identifier, which is equal to the name of the
    * graph in its default language settings. The values of the key are the various graph settings
    * associated with that graph.
    */
    private Map<String, Report> reports;

    /**
    * The expected revision number of the App settings (optional). The request will fail if the
    * revision number is not the latest revision. The revision will not be checked if this parameter
    * is null, or -1 is specified.
    */
    private Long revision;
}
