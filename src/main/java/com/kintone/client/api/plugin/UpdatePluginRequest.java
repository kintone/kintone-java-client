package com.kintone.client.api.plugin;

import com.kintone.client.api.KintoneRequest;
import java.nio.file.Path;
import lombok.Data;

/** A request object for Update Plug-in API. */
@Data
public class UpdatePluginRequest implements KintoneRequest {

    /** The ID of the Plug-in to be updated (required). */
    private String id;

    /**
     * The fileKey representing an uploaded file (required). Use {@link
     * com.kintone.client.FileClient#uploadFile(Path, String)} to upload the file and retrieve the
     * fileKey.
     */
    private String fileKey;
}
