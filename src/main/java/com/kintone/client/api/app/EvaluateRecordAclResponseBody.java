package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.EvaluatedRecordRight;
import java.util.List;
import lombok.Value;

/** A response object for Evaluate Record Acl API. */
@Value
public class EvaluateRecordAclResponseBody implements KintoneResponseBody {

    /** The list of objects that contain permission settings of the specified records. */
    private final List<EvaluatedRecordRight> rights;
}
