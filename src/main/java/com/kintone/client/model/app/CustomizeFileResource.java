package com.kintone.client.model.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kintone.client.model.FileBody;
import lombok.Data;

/** JavaScript and CSS customization information for uploaded files. */
@Data
@JsonIgnoreProperties(value = "type", allowGetters = true)
public class CustomizeFileResource implements CustomizeResource {

    /** The information of uploaded file. */
    private FileBody file;

    /** {@inheritDoc} */
    @Override
    public CustomizeType getType() {
        return CustomizeType.FILE;
    }
}
