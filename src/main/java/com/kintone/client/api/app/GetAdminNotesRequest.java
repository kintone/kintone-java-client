package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Get App Admin Notes API. */
@Data
public class GetAdminNotesRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;
}
