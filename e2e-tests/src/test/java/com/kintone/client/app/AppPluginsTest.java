package com.kintone.client.app;

import static org.assertj.core.api.Assertions.*;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.TestSettings;
import com.kintone.client.api.app.AddAppPluginsRequest;
import com.kintone.client.helper.App;
import com.kintone.client.model.app.AppPlugin;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class AppPluginsTest extends ApiTestBase {

    private KintoneClient client;
    private App app;
    private App appForGetPluginsPreview;

    @BeforeEach
    public void setupApp() {
        client = setupDefaultClient();
        Long testAppId = TestSettings.get().getTestAppId();
        Long testAppIdForGetPluginsPreview = TestSettings.get().getTestAppIdForGetPluginsPreview();
        if (testAppId != null) {
            app = App.fromExisting(client, testAppId);
        } else {
            throw new IllegalStateException(
                    "KINTONE_TEST_APP_ID is not set. Please create a test app and set the environment variable.");
        }
        if (testAppIdForGetPluginsPreview != null) {
            appForGetPluginsPreview = App.fromExisting(client, testAppIdForGetPluginsPreview);
        } else {
            throw new IllegalStateException(
                    "KINTONE_TEST_APP_ID_FOR_GET_PLUGINS_PREVIEW is not set. Please create a test app and set the environment variable.");
        }
    }

    @Test
    public void getPlugins_getPluginsPreview() throws IOException, InterruptedException {
        List<AppPlugin> pluginsForPreviewApp =
                client.app().getPluginsPreview(appForGetPluginsPreview.id(), "ja");
        List<AppPlugin> pluginsForLiveApp = client.app().getPlugins(appForGetPluginsPreview.id(), "ja");

        assertThat(pluginsForPreviewApp).hasSize(pluginsForLiveApp.size() + 1);
    }

    @Test
    @Disabled("Since plugins cannot be deleted from the app, they must be disabled temporarily.")
    public void addPlugins() throws IOException, InterruptedException {
        String pluginId = installTestPlugin(client, "plugin-c");

        try {
            List<AppPlugin> initialPlugins = client.app().getPluginsPreview(app.id(), "ja");
            boolean alreadyAdded = initialPlugins.stream().anyMatch(p -> p.getId().equals(pluginId));
            int initialPluginCount = initialPlugins.size();

            AddAppPluginsRequest request = new AddAppPluginsRequest();
            request.setApp(app.id());
            request.setIds(Arrays.asList(pluginId));
            client.app().addPlugins(request);

            List<AppPlugin> plugins = client.app().getPluginsPreview(app.id(), "ja");
            int expectedSize = alreadyAdded ? initialPluginCount : initialPluginCount + 1;
            assertThat(plugins).hasSize(expectedSize);

            assertThat(plugins.stream().anyMatch(p -> p.getId().equals(pluginId))).isTrue();
        } finally {
            client.plugin().uninstallPlugin(pluginId);
        }
    }

    private String installTestPlugin(KintoneClient client, String pluginName) throws IOException {
        Path path =
                new File(
                                AppPluginsTest.class
                                        .getResource("/com/kintone/client/plugin/" + pluginName + ".zip")
                                        .getFile())
                        .toPath();
        String fileKey = client.file().uploadFile(path, "multipart/form-data");
        return client.plugin().installPlugin(fileKey).getId();
    }
}
