package com.kintone.client.helper;

import com.kintone.client.api.app.UpdateAppSettingsRequest;
import com.kintone.client.model.FileBody;
import com.kintone.client.model.app.AppFileIcon;
import com.kintone.client.model.app.AppIcon;
import com.kintone.client.model.app.AppPresetIcon;

public class AppSettingsBuilder {
    private String name;
    private String description;
    private AppIcon icon;
    private String theme;
    private Long revision;

    public AppSettingsBuilder name(String name) {
        this.name = name;
        return this;
    }

    public AppSettingsBuilder description(String description) {
        this.description = description;
        return this;
    }

    public AppSettingsBuilder presetIcon(String icon) {
        this.icon = new AppPresetIcon().setKey(icon);
        return this;
    }

    public AppSettingsBuilder fileIcon(String fileKey) {
        this.icon = new AppFileIcon().setFile(new FileBody().setFileKey(fileKey));
        return this;
    }

    public AppSettingsBuilder theme(String theme) {
        this.theme = theme;
        return this;
    }

    public AppSettingsBuilder revision(Long revision) {
        this.revision = revision;
        return this;
    }

    UpdateAppSettingsRequest build(long appId) {
        UpdateAppSettingsRequest req = new UpdateAppSettingsRequest();
        req.setApp(appId);
        req.setName(name);
        req.setDescription(description);
        req.setTheme(theme);
        req.setIcon(icon);
        req.setRevision(revision);
        return req;
    }
}
