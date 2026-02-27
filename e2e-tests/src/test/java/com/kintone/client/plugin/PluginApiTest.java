package com.kintone.client.plugin;

import static org.assertj.core.api.Assertions.assertThat;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.api.plugin.GetInstalledPluginsRequest;
import com.kintone.client.api.plugin.GetInstalledPluginsResponseBody;
import com.kintone.client.api.plugin.InstallPluginRequest;
import com.kintone.client.api.plugin.InstallPluginResponseBody;
import com.kintone.client.api.plugin.UninstallPluginRequest;
import com.kintone.client.api.plugin.UninstallPluginResponseBody;
import com.kintone.client.api.plugin.UpdatePluginRequest;
import com.kintone.client.api.plugin.UpdatePluginResponseBody;
import com.kintone.client.model.app.DeployStatus;
import com.kintone.client.model.plugin.App;
import com.kintone.client.model.plugin.Plugin;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PluginApiTest extends ApiTestBase {

    private KintoneClient client;

    @BeforeEach
    public void setup() {
        super.setup();
        client = setupDefaultClient();
        uninstallTestPlugins(client);
    }

    @Test
    public void getInstalledPlugins() throws IOException {
        installTestPlugins(client);

        GetInstalledPluginsResponseBody resp = client.plugin().getInstalledPlugins();
        assertThat(resp.getPlugins()).hasSizeGreaterThanOrEqualTo(3);
        assertThat(resp.getPlugins().get(0).getName()).isEqualTo("plugin-c");
        assertThat(resp.getPlugins().get(1).getName()).isEqualTo("plugin-b");
        assertThat(resp.getPlugins().get(2).getName()).isEqualTo("plugin-a");

        Plugin plugin = resp.getPlugins().get(0);
        assertThat(plugin.getDescription()).isNotNull();

        resp = client.plugin().getInstalledPlugins(1L, 2L);
        assertThat(resp.getPlugins()).hasSize(2);
        assertThat(resp.getPlugins().get(0).getName()).isEqualTo("plugin-b");
        assertThat(resp.getPlugins().get(1).getName()).isEqualTo("plugin-a");

        uninstallTestPlugins(client);
    }

    @Test
    public void getInstalledPlugins_withIds() throws IOException {
        installTestPlugins(client);

        GetInstalledPluginsResponseBody allPlugins = client.plugin().getInstalledPlugins();
        assertThat(allPlugins.getPlugins()).hasSizeGreaterThanOrEqualTo(3);

        String pluginId1 = allPlugins.getPlugins().get(0).getId();
        String pluginId2 = allPlugins.getPlugins().get(2).getId();

        GetInstalledPluginsRequest req = new GetInstalledPluginsRequest();
        req.setIds(Arrays.asList(pluginId1, pluginId2));
        req.setLimit(100L);
        req.setOffset(0L);
        GetInstalledPluginsResponseBody resp = client.plugin().getInstalledPlugins(req);

        assertThat(resp.getPlugins()).hasSize(2);
        List<String> returnedIds =
                resp.getPlugins().stream().map(Plugin::getId).collect(Collectors.toList());
        assertThat(returnedIds).containsExactlyInAnyOrder(pluginId1, pluginId2);

        assertThat(resp.getPlugins().get(0).getDescription()).isNotNull();

        uninstallTestPlugins(client);
    }

    private void uninstallTestPlugins(KintoneClient client) {
        GetInstalledPluginsResponseBody resp = client.plugin().getInstalledPlugins(0L, 10L);
        for (Plugin plugin : resp.getPlugins()) {
            if (plugin.getName().startsWith("plugin-")) {
                client.plugin().uninstallPlugin(plugin.getId());
            }
        }
    }

    private void installTestPlugins(KintoneClient client) throws IOException {
        List<String> plugins = Arrays.asList("plugin-a", "plugin-b", "plugin-c");
        for (String p : plugins) {
            Path path = new File(PluginApiTest.class.getResource(p + ".zip").getFile()).toPath();
            String filekey = client.file().uploadFile(path, "multipart/form-data");
            try {
                client.plugin().installPlugin(filekey);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void installPlugin() throws IOException {
        Path path = new File(PluginApiTest.class.getResource("plugin-a.zip").getFile()).toPath();
        String fileKey = client.file().uploadFile(path, "multipart/form-data");
        InstallPluginRequest req = new InstallPluginRequest();
        req.setFileKey(fileKey);
        InstallPluginResponseBody resp = client.plugin().installPlugin(req);

        assertThat(resp.getId()).isNotNull();
        assertThat(resp.getVersion()).isNotNull();

        client.plugin().uninstallPlugin(resp.getId());
    }

    @Test
    public void updatePlugin() throws IOException {
        Path path = new File(PluginApiTest.class.getResource("plugin-a.zip").getFile()).toPath();
        String fileKey1 = client.file().uploadFile(path, "multipart/form-data");
        InstallPluginRequest req1 = new InstallPluginRequest();
        req1.setFileKey(fileKey1);
        InstallPluginResponseBody resp1 = client.plugin().installPlugin(req1);

        String fileKey2 = client.file().uploadFile(path, "multipart/form-data");
        UpdatePluginRequest req2 = new UpdatePluginRequest();
        req2.setId(resp1.getId());
        req2.setFileKey(fileKey2);
        UpdatePluginResponseBody resp2 = client.plugin().updatePlugin(req2);

        assertThat(resp2.getId()).isEqualTo(resp1.getId());
        assertThat(resp2.getVersion()).isNotNull();

        client.plugin().uninstallPlugin(resp2.getId());
    }

    @Test
    public void uninstallPlugin() throws IOException {
        Path path = new File(PluginApiTest.class.getResource("plugin-a.zip").getFile()).toPath();
        String fileKey = client.file().uploadFile(path, "multipart/form-data");
        InstallPluginRequest req = new InstallPluginRequest();
        req.setFileKey(fileKey);
        InstallPluginResponseBody resp = client.plugin().installPlugin(req);

        assertThat(resp.getId()).isNotNull();
        assertThat(resp.getVersion()).isNotNull();

        UninstallPluginRequest req2 = new UninstallPluginRequest();
        req2.setId(resp.getId());
        assertThat(client.plugin().uninstallPlugin(req2)).isEqualTo(new UninstallPluginResponseBody());
    }

    @Test
    public void getApps() throws IOException {
        Path path = new File(PluginApiTest.class.getResource("plugin-a.zip").getFile()).toPath();
        String fileKey = client.file().uploadFile(path, "multipart/form-data");
        InstallPluginRequest req = new InstallPluginRequest();
        req.setFileKey(fileKey);
        InstallPluginResponseBody resp = client.plugin().installPlugin(req);

        String pluginId = resp.getId();
        assertThat(pluginId).isNotNull();
        assertThat(resp.getVersion()).isNotNull();

        int numberOfApps = client.plugin().getApps(pluginId).size();

        String appName = "test-app";
        long appId = client.app().addApp(appName);
        client.app().addPlugins(appId, Arrays.asList(pluginId));

        List<App> respApps2 = client.plugin().getApps(pluginId);
        assertThat(respApps2).hasSize(numberOfApps + 1);
        assertThat(respApps2.stream().map(App::getId)).contains(appId);
        assertThat(respApps2.stream().map(App::getName)).contains(appName);

        client.plugin().uninstallPlugin(pluginId);
    }

    @Test
    public void getRequiredPlugins() throws InterruptedException, IOException {
        Path path = new File(PluginApiTest.class.getResource("plugin-a.zip").getFile()).toPath();
        String fileKey = client.file().uploadFile(path, "multipart/form-data");
        InstallPluginResponseBody installResp = client.plugin().installPlugin(fileKey);
        String pluginId = installResp.getId();

        long appId = client.app().addApp("test-app-for-required-plugins");
        client.app().addPlugins(appId, Arrays.asList(pluginId));
        waitForDeployApp(client, appId);

        client.plugin().uninstallPlugin(pluginId);

        boolean hasPlugin = false;
        for (Plugin plugin : client.plugin().getRequiredPlugins()) {
            if (plugin.getId().equals(pluginId)) {
                hasPlugin = true;
                break;
            }
        }
        assertThat(hasPlugin).isTrue();
    }

    private void waitForDeployApp(KintoneClient client, long appId) throws InterruptedException {
        client.app().deployApp(appId);
        while (true) {
            DeployStatus status = client.app().getDeployStatus(appId);
            if (status != DeployStatus.PROCESSING) {
                break;
            }
            Thread.sleep(500);
        }
    }
}
