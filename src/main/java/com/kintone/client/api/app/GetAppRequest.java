package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Get App API. */
@Data
public class GetAppRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long id;
}
