package com.kintone.client.model.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/** JavaScript and CSS customization information specified by an URL. */
@Data
@JsonIgnoreProperties(value = "type", allowGetters = true)
public class CustomizeUrlResource implements CustomizeResource {

    /** The URL. */
    private String url;

    /** {@inheritDoc} */
    @Override
    public CustomizeType getType() {
        return CustomizeType.URL;
    }
}
