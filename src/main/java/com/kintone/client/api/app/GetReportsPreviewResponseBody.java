package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.report.Report;
import java.util.Map;
import lombok.Value;

/** A response object for Get Graph Settings Preview API. */
@Value
public class GetReportsPreviewResponseBody implements KintoneResponseBody {

    /**
     * An object of Graph settings data. The key of the object is the graph's unique identifier, which
     * is set as the graph's name in its default language settings (this is regardless of the lang
     * request parameter's value). The values of the key are the various graph settings associated
     * with that graph.
     */
    private final Map<String, Report> reports;

    /** The revision number of the App settings. */
    private final long revision;
}
