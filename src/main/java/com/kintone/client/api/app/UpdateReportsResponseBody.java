package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.report.ReportId;
import java.util.Map;
import lombok.Value;

/** A response object for Update Graphs API. */
@Value
public class UpdateReportsResponseBody implements KintoneResponseBody {

    /** An object listing Graph information. */
    private final Map<String, ReportId> reports;

    /** The revision number of the App settings. */
    private final long revision;
}
