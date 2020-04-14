package com.kintone.client.api.record;

import com.kintone.client.api.KintoneRequest;
import com.kintone.client.model.record.StatusAction;
import java.util.List;
import lombok.Data;

/** A request object for Update Record Statuses API. */
@Data
public class UpdateRecordStatusesRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /** A list of objects including information of the status actions to proceed (required). */
    private List<StatusAction> records;
}
