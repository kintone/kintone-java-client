package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import com.kintone.client.model.app.AppDeployStatus;
import java.util.List;
import lombok.Value;

/** A response object for Get Deploy Status API. */
@Value
public class GetDeployStatusResponseBody implements KintoneResponseBody {

    /** The list of objects with data of deploy statuses. */
    private final List<AppDeployStatus> apps;
}
