package com.kintone.client.model.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/** An object that contain information of preset App icon. */
@Data
@JsonIgnoreProperties(value = "type", allowGetters = true)
public class AppPresetIcon implements AppIcon {

    /** The key identifier of the icon. */
    private String key;

    /** {@inheritDoc} */
    @Override
    public AppIconType getType() {
        return AppIconType.PRESET;
    }
}
