package com.kintone.client;

import com.kintone.client.api.plugin.*;
import com.kintone.client.api.plugin.GetAppsRequest;
import com.kintone.client.api.plugin.GetAppsResponseBody;
import com.kintone.client.api.plugin.InstallPluginRequest;
import com.kintone.client.api.plugin.InstallPluginResponseBody;
import com.kintone.client.api.plugin.UninstallPluginRequest;
import com.kintone.client.api.plugin.UninstallPluginResponseBody;
import com.kintone.client.api.plugin.UpdatePluginRequest;
import com.kintone.client.api.plugin.UpdatePluginResponseBody;
import com.kintone.client.model.plugin.App;
import com.kintone.client.model.plugin.Plugin;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/** A client that operates plugin APIs. */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PluginClient {

    private final InternalClient client;
    private final List<ResponseHandler> handlers;

    /**
     * Gets the list of plug-ins imported into Kintone.
     *
     * @return the response data. See {@link GetInstalledPluginsResponseBody}
     */
    public GetInstalledPluginsResponseBody getInstalledPlugins() {
        return getInstalledPlugins(null, null);
    }

    /**
     * Gets the list of plug-ins imported into Kintone.
     *
     * @param offset The number of plug-ins to skip from the list of installed plug-ins.
     * @param limit The maximum number of plug-ins to retrieve.
     * @return the response data. See {@link GetInstalledPluginsResponseBody}
     */
    public GetInstalledPluginsResponseBody getInstalledPlugins(Long offset, Long limit) {
        GetInstalledPluginsRequest request = new GetInstalledPluginsRequest();
        request.setOffset(offset);
        request.setLimit(limit);
        return getInstalledPlugins(request);
    }

    /**
     * Gets the list of plug-ins imported into Kintone.
     *
     * @param request the request parameters. See {@link GetInstalledPluginsRequest}
     * @return the response data. See {@link GetInstalledPluginsResponseBody}
     */
    public GetInstalledPluginsResponseBody getInstalledPlugins(GetInstalledPluginsRequest request) {
        return client.call(KintoneApi.GET_PLUGINS, request, handlers);
    }

    /**
     * Installs a Plug-in into Kintone.
     *
     * @param fileKey the fileKey representing an uploaded file.
     * @return the response data. See {@link InstallPluginResponseBody}
     */
    public InstallPluginResponseBody installPlugin(String fileKey) {
        InstallPluginRequest request = new InstallPluginRequest();
        request.setFileKey(fileKey);
        return installPlugin(request);
    }

    /**
     * Installs a Plug-in into Kintone.
     *
     * @param request the request parameters. See {@link InstallPluginRequest}
     * @return the response data. See {@link InstallPluginResponseBody}
     */
    public InstallPluginResponseBody installPlugin(InstallPluginRequest request) {
        return client.call(KintoneApi.INSTALL_PLUGIN, request, handlers);
    }

    /**
     * Uninstalls a Plug-in from the Kintone environment.
     *
     * @param id the Plug-in ID.
     */
    public void uninstallPlugin(String id) {
        UninstallPluginRequest request = new UninstallPluginRequest();
        request.setId(id);
        uninstallPlugin(request);
    }

    /**
     * Uninstalls a Plug-in from the Kintone environment.
     *
     * @param request the request parameters. See {@link UninstallPluginRequest}
     * @return the response data. See {@link UninstallPluginResponseBody}
     */
    public UninstallPluginResponseBody uninstallPlugin(UninstallPluginRequest request) {
        return client.call(KintoneApi.UNINSTALL_PLUGIN, request, handlers);
    }

    /**
     * Updates an installed Plug-in in the Kintone environment.
     *
     * @param id the Plug-in ID.
     * @param fileKey the fileKey representing an uploaded file.
     * @return the response data. See {@link UpdatePluginResponseBody}
     */
    public UpdatePluginResponseBody updatePlugin(String id, String fileKey) {
        UpdatePluginRequest request = new UpdatePluginRequest();
        request.setId(id);
        request.setFileKey(fileKey);
        return updatePlugin(request);
    }

    /**
     * Updates an installed Plug-in in the Kintone environment.
     *
     * @param request the request parameters. See {@link UpdatePluginRequest}
     * @return the response data. See {@link UpdatePluginResponseBody}
     */
    public UpdatePluginResponseBody updatePlugin(UpdatePluginRequest request) {
        return client.call(KintoneApi.UPDATE_PLUGIN, request, handlers);
    }

    /**
     * Gets the list of Apps that have the specified Plug-in added.
     *
     * @param id The ID of the plug-in.
     * @return A list of objects containing the App ID and name.
     */
    public List<App> getApps(String id) {
        return getApps(id, null, null);
    }

    /**
     * Gets the list of Apps that have the specified Plug-in added.
     *
     * @param id The ID of the plug-in.
     * @param offset The maximum number of plug-ins to retrieve.
     * @param limit The number of plug-ins to skip from the list of plug-ins.
     * @return A list of objects containing the App ID and name.
     */
    public List<App> getApps(String id, Long offset, Long limit) {
        GetAppsRequest request = new GetAppsRequest();
        request.setId(id);
        request.setLimit(limit);
        request.setOffset(offset);
        return getApps(request).getApps();
    }

    /**
     * Gets the list of Apps that have the specified Plug-in added.
     *
     * @param request the request parameters. See {@link GetAppsRequest}
     * @return the response data. See {@link GetAppsResponseBody}
     */
    public GetAppsResponseBody getApps(GetAppsRequest request) {
        return client.call(KintoneApi.GET_APPS_PLUGIN_ADDED, request, handlers);
    }

    /**
     * Gets the list of plug-ins that have been deleted from Kintone, but have already been added to
     * Apps.
     *
     * @return A list of Plug-ins that needs to be installed.
     */
    public List<Plugin> getRequiredPlugins() {
        return getRequiredPlugins(null, null);
    }

    /**
     * Gets the list of plug-ins that have been deleted from Kintone, but have already been added to
     * Apps.
     *
     * @param offset The number of plug-ins to skip from the list of required plug-ins.
     * @param limit The maximum number of plug-ins to retrieve.
     * @return A list of Plug-ins that needs to be installed.
     */
    public List<Plugin> getRequiredPlugins(Long offset, Long limit) {
        GetRequiredPluginsRequest request = new GetRequiredPluginsRequest();
        request.setOffset(offset);
        request.setLimit(limit);
        return getRequiredPlugins(request).getPlugins();
    }

    /**
     * Gets the list of plug-ins that have been deleted from Kintone, but have already been added to
     * Apps.
     *
     * @param request the request parameters. See {@link GetRequiredPluginsRequest}
     * @return the response data. See {@link GetRequiredPluginsResponseBody}
     */
    public GetRequiredPluginsResponseBody getRequiredPlugins(GetRequiredPluginsRequest request) {
        return client.call(KintoneApi.GET_REQUIRED_PLUGINS, request, handlers);
    }
}
