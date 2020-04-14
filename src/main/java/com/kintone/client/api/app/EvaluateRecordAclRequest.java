package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import java.util.List;
import lombok.Data;

/** A request object for Evaluate Record Acl API. */
@Data
public class EvaluateRecordAclRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** The list of record IDs that will be evaluated (required). */
    private List<Long> ids;
}
