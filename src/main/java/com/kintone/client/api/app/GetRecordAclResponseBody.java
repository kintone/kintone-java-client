package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.RecordRight;
import java.util.List;
import lombok.Value;

/** A response object for Get Record Acl API. */
@Value
public class GetRecordAclResponseBody implements KintoneResponseBody {

    /** The list of objects that contain data of record permissions, in order of priority. */
    private final List<RecordRight> rights;

    /** The revision number of the App settings. */
    private final long revision;
}
