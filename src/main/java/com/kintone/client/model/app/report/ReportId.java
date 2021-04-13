package com.kintone.client.model.app.report;

import lombok.Value;

/** Created or updated graph ID returned by Update Graphs API. */
@Value
public class ReportId {

    /** The ID of the graph. */
    private final long id;
}
