package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.layout.Layout;
import java.util.List;
import lombok.Value;

/** A response object for Get Form Layout API. */
@Value
public class GetFormLayoutResponseBody implements KintoneResponseBody {

    /** The list of field layouts for each row. */
    private final List<Layout> layout;

    /** The revision number of the App settings. */
    private final long revision;
}
