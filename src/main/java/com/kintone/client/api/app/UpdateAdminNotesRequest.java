package com.kintone.client.api.app;

import com.kintone.client.api.KintoneRequest;
import lombok.Data;

/** A request object for Update App Admin Notes API. */
@Data
public class UpdateAdminNotesRequest implements KintoneRequest {

    /** The App ID (required). */
    private Long app;

    /**
     * The content of the notes (optional). The content will not be updated if this parameter is null.
     */
    private String content;

    /**
     * The permission settings to include this note in app templates or duplicates (optional). The
     * content will not be updated if this parameter is null.
     */
    private Boolean includeInTemplateAndDuplicates;

    /**
     * The expected revision number of the App settings (optional). The request will fail if the
     * revision number is not the latest revision. The revision will not be checked if this parameter
     * is null, or -1 is specified.
     */
    private Long revision;
}
