package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Get Form Layout API. */
@Data
public class GetFormLayoutRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;
}
