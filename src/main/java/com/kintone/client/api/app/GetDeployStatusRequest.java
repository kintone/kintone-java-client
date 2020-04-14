package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import java.util.List;
import lombok.Data;

/** A request object for Get Deploy Status API. */
@Data
public class GetDeployStatusRequest implements KintoneRequest {

    /** The list of Apps to check the deploy statuses of (required). */
    private List<Long> apps;
}
