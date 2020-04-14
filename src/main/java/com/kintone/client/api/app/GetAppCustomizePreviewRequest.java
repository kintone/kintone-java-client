package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Get App Customize Preview API. */
@Data
public class GetAppCustomizePreviewRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;
}
