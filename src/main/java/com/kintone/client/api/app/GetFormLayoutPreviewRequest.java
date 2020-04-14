package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Get Form Layout Preview API. */
@Data
public class GetFormLayoutPreviewRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;
}
