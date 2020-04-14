package com.kintone.client.model.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kintone.client.model.FileBody;
import lombok.Data;

/** An object that contain information of file-type App icon. */
@Data
@JsonIgnoreProperties(value = "type", allowGetters = true)
public class AppFileIcon implements AppIcon {

    /** An object containing information of uploaded icon files. */
    private FileBody file;

    /** {@inheritDoc} */
    @Override
    public AppIconType getType() {
        return AppIconType.FILE;
    }
}
