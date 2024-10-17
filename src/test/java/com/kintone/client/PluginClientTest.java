package com.kintone.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.api.plugin.*;
import com.kintone.client.model.plugin.App;
import com.kintone.client.model.plugin.Plugin;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PluginClientTest {

    private InternalClientMock mockClient = new InternalClientMock();
    private PluginClient sut = new PluginClient(mockClient, Collections.emptyList());

    @Test
    public void getInstalledPlugins() {
        List<Plugin> plugins = Collections.emptyList();
        GetInstalledPluginsResponseBody resp = new GetInstalledPluginsResponseBody(plugins);
        mockClient.setResponseBody(resp);

        assertThat(sut.getInstalledPlugins()).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_PLUGINS);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetInstalledPluginsRequest());
    }

    @Test
    public void getInstalledPlugins_Long() {
        List<Plugin> plugins = Collections.emptyList();
        GetInstalledPluginsResponseBody resp = new GetInstalledPluginsResponseBody(plugins);
        mockClient.setResponseBody(resp);

        assertThat(sut.getInstalledPlugins(0L, 100L)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_PLUGINS);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetInstalledPluginsRequest().setOffset(0L).setLimit(100L));
    }

    @Test
    public void getInstalledPlugins_Request() {
        GetInstalledPluginsRequest req = new GetInstalledPluginsRequest();
        req.setOffset(5L);
        req.setLimit(50L);
        List<Plugin> plugins = Collections.emptyList();
        GetInstalledPluginsResponseBody resp = new GetInstalledPluginsResponseBody(plugins);
        mockClient.setResponseBody(resp);

        assertThat(sut.getInstalledPlugins(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_PLUGINS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void installPlugin_String() {
        InstallPluginResponseBody resp = new InstallPluginResponseBody("1", "2");
        mockClient.setResponseBody(resp);

        assertThat(sut.installPlugin("fileKey")).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.INSTALL_PLUGIN);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new InstallPluginRequest().setFileKey("fileKey"));
    }

    @Test
    public void installPlugin_Request() {
        InstallPluginRequest req = new InstallPluginRequest();
        req.setFileKey("fileKey");
        InstallPluginResponseBody resp = new InstallPluginResponseBody("1", "2");
        mockClient.setResponseBody(resp);

        assertThat(sut.installPlugin(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.INSTALL_PLUGIN);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void updatePlugin_String_String() {
        UpdatePluginResponseBody resp = new UpdatePluginResponseBody("1", "2");
        mockClient.setResponseBody(resp);

        assertThat(sut.updatePlugin("id", "fileKey")).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_PLUGIN);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new UpdatePluginRequest().setId("id").setFileKey("fileKey"));
    }

    @Test
    public void updatePlugin_Request() {
        UpdatePluginRequest req = new UpdatePluginRequest();
        req.setId("id");
        req.setFileKey("fileKey");
        UpdatePluginResponseBody resp = new UpdatePluginResponseBody("1", "2");
        mockClient.setResponseBody(resp);

        assertThat(sut.updatePlugin(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UPDATE_PLUGIN);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void uninstallPlugin_String() {
        mockClient.setResponseBody(new UninstallPluginResponseBody());

        sut.uninstallPlugin("id");
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UNINSTALL_PLUGIN);
        assertThat(mockClient.getLastBody()).isEqualTo(new UninstallPluginRequest().setId("id"));
    }

    @Test
    public void uninstallPlugin_Request() {
        UninstallPluginRequest req = new UninstallPluginRequest();
        req.setId("id");
        UninstallPluginResponseBody resp = new UninstallPluginResponseBody();
        mockClient.setResponseBody(resp);

        assertThat(sut.uninstallPlugin(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.UNINSTALL_PLUGIN);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getApps() {
        List<App> apps = Collections.emptyList();
        GetAppsResponseBody resp = new GetAppsResponseBody(apps);
        mockClient.setResponseBody(resp);

        assertThat(sut.getApps("123")).isEqualTo(apps);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APPS_PLUGIN_ADDED);
        assertThat(mockClient.getLastBody()).isEqualTo(new GetAppsRequest().setId("123"));
    }

    @Test
    public void getApps_Long() {
        List<App> apps = Collections.emptyList();
        GetAppsResponseBody resp = new GetAppsResponseBody(apps);
        mockClient.setResponseBody(resp);

        assertThat(sut.getApps("123", 0L, 100L)).isEqualTo(apps);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APPS_PLUGIN_ADDED);
        assertThat(mockClient.getLastBody())
                .isEqualTo(new GetAppsRequest().setId("123").setOffset(0L).setLimit(100L));
    }

    @Test
    public void getApps_Request() {
        GetAppsRequest req = new GetAppsRequest();
        req.setId("123");
        req.setOffset(5L);
        req.setLimit(50L);
        List<App> apps = Collections.emptyList();
        GetAppsResponseBody resp = new GetAppsResponseBody(apps);
        mockClient.setResponseBody(resp);

        assertThat(sut.getApps(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_APPS_PLUGIN_ADDED);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getRequiredPlugins() {
        GetRequiredPluginsRequest req = new GetRequiredPluginsRequest();
        List<Plugin> plugins = Collections.emptyList();
        GetRequiredPluginsResponseBody resp = new GetRequiredPluginsResponseBody(plugins);
        mockClient.setResponseBody(resp);

        assertThat(sut.getRequiredPlugins()).isEqualTo(plugins);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_REQUIRED_PLUGINS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getRequiredPlugins_Long() {
        GetRequiredPluginsRequest req = new GetRequiredPluginsRequest();
        req.setOffset(0L);
        req.setLimit(100L);
        List<Plugin> plugins = Collections.emptyList();
        GetRequiredPluginsResponseBody resp = new GetRequiredPluginsResponseBody(plugins);
        mockClient.setResponseBody(resp);

        assertThat(sut.getRequiredPlugins(0L, 100L)).isEqualTo(plugins);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_REQUIRED_PLUGINS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }

    @Test
    public void getRequiredPlugins_Request() {
        GetRequiredPluginsRequest req = new GetRequiredPluginsRequest();
        req.setOffset(5L);
        req.setLimit(50L);
        List<Plugin> plugins = Collections.emptyList();
        GetRequiredPluginsResponseBody resp = new GetRequiredPluginsResponseBody(plugins);
        mockClient.setResponseBody(resp);

        assertThat(sut.getRequiredPlugins(req)).isEqualTo(resp);
        assertThat(mockClient.getLastApi()).isEqualTo(KintoneApi.GET_REQUIRED_PLUGINS);
        assertThat(mockClient.getLastBody()).isEqualTo(req);
    }
}
