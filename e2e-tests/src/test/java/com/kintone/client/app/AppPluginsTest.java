package com.kintone.client.app;

import static org.assertj.core.api.Assertions.*;

import com.kintone.client.ApiTestBase;
import com.kintone.client.KintoneClient;
import com.kintone.client.api.app.AddAppPluginsRequest;
import com.kintone.client.helper.App;
import com.kintone.client.model.app.AppPlugin;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class AppPluginsTest extends ApiTestBase {

    @Test
    public void getPlugins_getPluginsPreview() throws IOException, InterruptedException {
        KintoneClient client = setupDefaultClient();

        String plugin1Id = installTestPlugin(client, "plugin-a");
        Thread.sleep(1000);
        String plugin2Id = installTestPlugin(client, "plugin-b");

        try {
            App app = App.create(client, "getPlugins_getPluginsPreview");

            AddAppPluginsRequest addReq = new AddAppPluginsRequest();
            addReq.setApp(app.id());
            addReq.setIds(Arrays.asList(plugin1Id));
            client.app().addPlugins(addReq);
            app.deploy();

            AddAppPluginsRequest addReq2 = new AddAppPluginsRequest();
            addReq2.setApp(app.id());
            addReq2.setIds(Arrays.asList(plugin2Id));
            client.app().addPlugins(addReq2);

            List<AppPlugin> pluginsForPreviewApp = client.app().getPluginsPreview(app.id(), "ja");
            List<AppPlugin> pluginsForLiveApp = client.app().getPlugins(app.id(), "ja");

            assertThat(pluginsForPreviewApp).hasSize(2);
            assertThat(pluginsForLiveApp).hasSize(1);
        } finally {
            client.plugin().uninstallPlugin(plugin1Id);
            client.plugin().uninstallPlugin(plugin2Id);
        }
    }

    @Test
    public void addPlugins() throws IOException, InterruptedException {
        KintoneClient client = setupDefaultClient();

        String pluginId = installTestPlugin(client, "plugin-a");

        try {
            App app = App.create(client, "addPlugins");
            assertThat(client.app().getPluginsPreview(app.id(), "ja")).hasSize(0);

            AddAppPluginsRequest request = new AddAppPluginsRequest();
            request.setApp(app.id());
            request.setIds(Arrays.asList(pluginId));
            client.app().addPlugins(request);

            List<AppPlugin> plugins = client.app().getPluginsPreview(app.id(), "ja");
            assertThat(plugins).hasSize(1);
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
