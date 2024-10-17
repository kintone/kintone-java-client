package com.kintone.client.api.app;

import com.kintone.client.api.KintoneResponseBody;
import lombok.Value;

/** A response object for Get App Admin Notes Preview API. */
@Value
public class GetAdminNotesPreviewResponseBody implements KintoneResponseBody {

    /** The content of the notes. If not set, an empty string is returned. */
    private final String content;

    /** The permission settings to include this note in app templates or duplicates. */
    private final boolean includeInTemplateAndDuplicates;

    /** The revision number of the App settings. */
    private final long revision;
}
