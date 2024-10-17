package com.kintone.client.api.plugin;

import com.kintone.client.api.KintoneRequest;
import java.nio.file.Path;
import lombok.Data;

/** A request object for Install Plug-in API. */
@Data
public class InstallPluginRequest implements KintoneRequest {

    /**
     * The fileKey representing an uploaded file (required). Use {@link
     * com.kintone.client.FileClient#uploadFile(Path, String)} to upload the file and retrieve the
     * fileKey.
     */
    private String fileKey;
}
