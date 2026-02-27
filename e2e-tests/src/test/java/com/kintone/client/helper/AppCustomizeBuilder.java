package com.kintone.client.helper;

import com.kintone.client.api.app.UpdateAppCustomizeRequest;
import com.kintone.client.model.FileBody;
import com.kintone.client.model.app.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppCustomizeBuilder {

    private enum ResourceType {
        DESKTOP_JS,
        DESKTOP_CSS,
        MOBILE_JS,
        MOBILE_CSS
    }

    private final Map<ResourceType, List<CustomizeResource>> resources = new HashMap<>();
    private CustomizeScope scope;
    private Long revision;

    private AppCustomizeBuilder addResource(ResourceType type, CustomizeResource resource) {
        resources.putIfAbsent(type, new ArrayList<>());
        resources.get(type).add(resource);
        return this;
    }

    public AppCustomizeBuilder scope(CustomizeScope scope) {
        this.scope = scope;
        return this;
    }

    public AppCustomizeBuilder desktopJsFile(String fileKey) {
        FileBody file = new FileBody().setFileKey(fileKey);
        return addResource(ResourceType.DESKTOP_JS, new CustomizeFileResource().setFile(file));
    }

    public AppCustomizeBuilder desktopJsUrl(String url) {
        return addResource(ResourceType.DESKTOP_JS, new CustomizeUrlResource().setUrl(url));
    }

    public AppCustomizeBuilder desktopCssFile(String fileKey) {
        FileBody file = new FileBody().setFileKey(fileKey);
        return addResource(ResourceType.DESKTOP_CSS, new CustomizeFileResource().setFile(file));
    }

    public AppCustomizeBuilder desktopCssUrl(String url) {
        return addResource(ResourceType.DESKTOP_CSS, new CustomizeUrlResource().setUrl(url));
    }

    public AppCustomizeBuilder mobileJsFile(String fileKey) {
        FileBody file = new FileBody().setFileKey(fileKey);
        return addResource(ResourceType.MOBILE_JS, new CustomizeFileResource().setFile(file));
    }

    public AppCustomizeBuilder mobileJsUrl(String url) {
        return addResource(ResourceType.MOBILE_JS, new CustomizeUrlResource().setUrl(url));
    }

    public AppCustomizeBuilder mobileCssFile(String fileKey) {
        FileBody file = new FileBody().setFileKey(fileKey);
        return addResource(ResourceType.MOBILE_CSS, new CustomizeFileResource().setFile(file));
    }

    public AppCustomizeBuilder mobileCssUrl(String url) {
        return addResource(ResourceType.MOBILE_CSS, new CustomizeUrlResource().setUrl(url));
    }

    public AppCustomizeBuilder revision(Long revision) {
        this.revision = revision;
        return this;
    }

    UpdateAppCustomizeRequest build(long appId) {
        List<CustomizeResource> desktopJs = resources.get(ResourceType.DESKTOP_JS);
        List<CustomizeResource> desktopCss = resources.get(ResourceType.DESKTOP_CSS);
        List<CustomizeResource> mobileJs = resources.get(ResourceType.MOBILE_JS);
        List<CustomizeResource> mobileCss = resources.get(ResourceType.MOBILE_CSS);

        UpdateAppCustomizeRequest req = new UpdateAppCustomizeRequest();
        req.setApp(appId);
        req.setScope(scope);
        if (desktopJs != null || desktopCss != null) {
            req.setDesktop(new CustomizeBody().setJs(desktopJs).setCss(desktopCss));
        }
        if (mobileJs != null || mobileCss != null) {
            req.setMobile(new CustomizeBody().setJs(mobileJs).setCss(mobileCss));
        }
        req.setRevision(revision);
        return req;
    }
}
